package ru.salon.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.salon.model.MasterPerformance;
import ru.salon.service.DashboardService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/all")
    public MasterPerformance getIncomesAndCost() {
        return dashboardService.getAllIncomesAndCosts();
    }

    @GetMapping("/mastersBetweenStart")
    public List<MasterPerformance> getIncomesAndCostByMasterBetweenStart(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String start,
                                             @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String end) {
        Instant startSlot = LocalDateTime.parse(start, formatter).atZone(ZoneId.of("+0")).toInstant();
        Instant endSlot = LocalDateTime.parse(end, formatter).atZone(ZoneId.of("+0")).toInstant();
        return dashboardService.getMastersIncomesAndCosts(startSlot, endSlot);
    }
}
