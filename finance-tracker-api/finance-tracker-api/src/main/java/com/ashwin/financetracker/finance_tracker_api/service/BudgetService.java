package com.ashwin.financetracker.finance_tracker_api.service;

import com.ashwin.financetracker.finance_tracker_api.dto.BudgetDto;
import com.ashwin.financetracker.finance_tracker_api.entity.Budget;
import com.ashwin.financetracker.finance_tracker_api.entity.Category;
import com.ashwin.financetracker.finance_tracker_api.entity.User;
import com.ashwin.financetracker.finance_tracker_api.repository.BudgetRepository;
import com.ashwin.financetracker.finance_tracker_api.repository.CategoryRepository;
import com.ashwin.financetracker.finance_tracker_api.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public BudgetService(BudgetRepository budgetRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Set or Update a Budget
    public Budget createOrUpdateBudget(BudgetDto budgetDto) {
        User user = getAuthenticatedUser();
        
        // 1. Fetch the category
        Category category = categoryRepository.findById(budgetDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 2. CRITICAL SECURITY CHECK: Does this category actually belong to this user?
        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: Category belongs to another user");
        }

        // 3. Upsert Logic: Find existing budget for this month, or create a new one
        Budget budget = budgetRepository.findByUserIdAndCategoryIdAndMonthYear(
                user.getId(), category.getId(), budgetDto.getMonthYear()
        ).orElse(new Budget());

        // 4. Set values and save
        budget.setUser(user);
        budget.setCategory(category);
        budget.setLimitAmount(budgetDto.getLimitAmount());
        budget.setMonthYear(budgetDto.getMonthYear());

        return budgetRepository.save(budget);
    }

    // Get all Budgets for the logged-in user
    public List<Budget> getUserBudgets() {
        return budgetRepository.findByUserId(getAuthenticatedUser().getId());
    }
}