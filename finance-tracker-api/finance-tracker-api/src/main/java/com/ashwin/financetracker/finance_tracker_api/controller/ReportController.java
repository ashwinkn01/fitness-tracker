package com.ashwin.financetracker.finance_tracker_api.controller;

import com.ashwin.financetracker.finance_tracker_api.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Example request: GET /api/reports/export?month=2026-07&format=pdf
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportReport(
            @RequestParam String month,
            @RequestParam(defaultValue = "csv") String format) {

        try {
            byte[] reportData;
            String contentType;
            String filename;

            // 1. Determine which format the user requested
            if ("pdf".equalsIgnoreCase(format)) {
                reportData = reportService.generatePdfReport(month);
                contentType = MediaType.APPLICATION_PDF_VALUE; // "application/pdf"
                filename = "transaction-report-" + month + ".pdf";
            } else {
                reportData = reportService.generateCsvReport(month);
                contentType = "text/csv";
                filename = "transaction-report-" + month + ".csv";
            }

            // 2. Package the bytes with the correct headers to trigger a file download
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(reportData);

        } catch (Exception e) {
            // If anything goes wrong, return a 500 Internal Server Error without crashing the app
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}