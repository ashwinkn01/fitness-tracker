package com.ashwin.financetracker.finance_tracker_api.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class BudgetDto {
    private Long id;
    private Long categoryId;
    private BigDecimal limitAmount; 
    private String monthYear; // Format expected: "YYYY-MM"
}