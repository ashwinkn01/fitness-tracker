package com.ashwin.financetracker.finance_tracker_api.dto;

import com.ashwin.financetracker.finance_tracker_api.entity.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TransactionDto {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDate txnDate;
    private LocalTime txnTime;
    private String note;
    private Long categoryId;
}