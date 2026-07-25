package com.ashwin.financetracker.finance_tracker_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ashwin.financetracker.finance_tracker_api.entity.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    
}
