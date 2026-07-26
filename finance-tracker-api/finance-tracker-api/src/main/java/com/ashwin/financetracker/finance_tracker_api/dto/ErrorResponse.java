package com.ashwin.financetracker.finance_tracker_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int status;           // HTTP Status Code (e.g., 400, 403, 404)
    private String error;         // Short error type (e.g., "Bad Request")
    private String message;       // The specific message from your Service layer
    private LocalDateTime timestamp; // When the error occurred
}