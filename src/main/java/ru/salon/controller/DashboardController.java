package ru.salon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.salon.model.MasterPerformance;
import ru.salon.service.DashboardService;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/masters")
    public List<MasterPerformance> getIncomesAndCostByMaster() {
        return dashboardService.getMastersIncomesAndCosts();
    }

    @GetMapping("/all")
    public MasterPerformance getIncomesAndCost() {
        return dashboardService.getAllIncomesAndCosts();
    }
}
