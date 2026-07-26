package com.ashwin.financetracker.finance_tracker_api.controller;

import com.ashwin.financetracker.finance_tracker_api.dto.BudgetDto;
import com.ashwin.financetracker.finance_tracker_api.entity.Budget;
import com.ashwin.financetracker.finance_tracker_api.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<Budget> setBudget(@RequestBody BudgetDto budgetDto) {
        Budget savedBudget = budgetService.createOrUpdateBudget(budgetDto);
        return ResponseEntity.ok(savedBudget);
    }

    @GetMapping
    public ResponseEntity<List<Budget>> getUserBudgets() {
        List<Budget> budgets = budgetService.getUserBudgets();
        return ResponseEntity.ok(budgets);
    }
}