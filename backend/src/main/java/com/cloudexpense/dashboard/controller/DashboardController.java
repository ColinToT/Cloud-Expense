package com.cloudexpense.dashboard.controller;

import com.cloudexpense.dashboard.dto.DashboardResponse;
import com.cloudexpense.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: DashboardController
 * Package: com.cloudexpense.dashboard.controller
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/8/5 21:03
 * @Version: v1.0
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(){
        return ResponseEntity.ok(
                dashboardService.getDashboard()
        );
    }
}
