package com.ashwin.financetracker.finance_tracker_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ashwin.financetracker.finance_tracker_api.entity.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    // Fetch all budgets for the dashboard
    List<Budget> findByUserId(Long userId);
    
    // Used to check if a budget already exists before creating a new one
    Optional<Budget> findByUserIdAndCategoryIdAndMonthYear(Long userId, Long categoryId, String monthYear);

    // Fetch all budgets for a user in a specific month
    List<Budget> findByUserIdAndMonthYear(Long userId, String monthYear);
}
