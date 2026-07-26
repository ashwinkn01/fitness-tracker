package com.ashwin.financetracker.finance_tracker_api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CategoryBreakdownDto {
    private String categoryName;
    private BigDecimal totalSpent;
    private BigDecimal budgetLimit;
    private Double percentUsed;
    private boolean overBudget;
}