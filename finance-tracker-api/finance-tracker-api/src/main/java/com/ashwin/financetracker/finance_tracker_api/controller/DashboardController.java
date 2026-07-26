package com.ashwin.financetracker.finance_tracker_api.controller;

import com.ashwin.financetracker.finance_tracker_api.dto.DashboardSummaryDto;
import com.ashwin.financetracker.finance_tracker_api.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // Example request: GET /api/dashboard/summary?month=2026-07
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDto> getSummary(@RequestParam String month) {
        DashboardSummaryDto summary = dashboardService.getDashboardSummary(month);
        return ResponseEntity.ok(summary);
    }
}