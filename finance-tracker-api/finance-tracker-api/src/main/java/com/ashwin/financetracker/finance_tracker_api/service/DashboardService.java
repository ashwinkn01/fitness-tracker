package com.ashwin.financetracker.finance_tracker_api.service;

import com.ashwin.financetracker.finance_tracker_api.dto.CategoryBreakdownDto;
import com.ashwin.financetracker.finance_tracker_api.dto.DashboardSummaryDto;
import com.ashwin.financetracker.finance_tracker_api.entity.Budget;
import com.ashwin.financetracker.finance_tracker_api.entity.Transaction;
import com.ashwin.financetracker.finance_tracker_api.entity.TransactionType;
import com.ashwin.financetracker.finance_tracker_api.entity.User;
import com.ashwin.financetracker.finance_tracker_api.repository.BudgetRepository;
import com.ashwin.financetracker.finance_tracker_api.repository.TransactionRepository;
import com.ashwin.financetracker.finance_tracker_api.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    public DashboardService(TransactionRepository transactionRepository, BudgetRepository budgetRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public DashboardSummaryDto getDashboardSummary(String monthYear) {
        User user = getAuthenticatedUser();
        
        // 1. Calculate the first and last day of the requested month (e.g., "2026-07")
        YearMonth yearMonth = YearMonth.parse(monthYear);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // 2. Fetch all data for this month
        List<Transaction> transactions = transactionRepository.findByUserIdAndTxnDateBetween(user.getId(), startDate, endDate);
        List<Budget> budgets = budgetRepository.findByUserIdAndMonthYear(user.getId(), monthYear);

        // 3. Calculate Overall Totals
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;

        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.INCOME) {
                totalIncome = totalIncome.add(t.getAmount());
            } else if (t.getType() == TransactionType.EXPENSE) {
                totalExpenses = totalExpenses.add(t.getAmount());
            }
        }
        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        // 4. Group Expenses by Category
        Map<String, BigDecimal> expensesByCategory = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        // 5. Build Category Breakdown & Budget Alerts
        List<CategoryBreakdownDto> breakdownList = new ArrayList<>();
        
        for (Map.Entry<String, BigDecimal> entry : expensesByCategory.entrySet()) {
            String categoryName = entry.getKey();
            BigDecimal amountSpent = entry.getValue();

            // Find the budget limit for this category (default to 0 if no budget set)
            BigDecimal budgetLimit = budgets.stream()
                    .filter(b -> b.getCategory().getName().equals(categoryName))
                    .map(Budget::getLimitAmount)
                    .findFirst()
                    .orElse(BigDecimal.ZERO);

            // Calculate percentage and overBudget flag
            Double percentUsed = 0.0;
            boolean overBudget = false;

            if (budgetLimit.compareTo(BigDecimal.ZERO) > 0) {
                // (Amount Spent / Budget Limit) * 100
                percentUsed = amountSpent.divide(budgetLimit, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue();
                
                overBudget = percentUsed > 100.0;
            }

            breakdownList.add(CategoryBreakdownDto.builder()
                    .categoryName(categoryName)
                    .totalSpent(amountSpent)
                    .budgetLimit(budgetLimit)
                    .percentUsed(percentUsed)
                    .overBudget(overBudget)
                    .build());
        }

        // 6. Return the fully packaged DTO
        return DashboardSummaryDto.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(netBalance)
                .categoryBreakdown(breakdownList)
                .build();
    }
}