package com.project.frontend.controller;

import com.project.frontend.client.BackendClient;
import com.project.frontend.dto.RecentOrder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class DashboardController {

    private final BackendClient backendClient;

    @GetMapping("/")
    public String dashboard(Model model) {

        // Stats cards
        Map<String, Object> stats = backendClient.getDashboardStats();
        model.addAttribute("stats", stats);

        // Recent orders table
        List<RecentOrder> recentOrders = backendClient.getRecentOrders();
        model.addAttribute("recentOrders", recentOrders);

        // Chart.js bar chart — orders per month
        Map<String, Long> raw = backendClient.getOrdersPerMonth();
        // Keep insertion order; backend may already return in month order
        List<String> chartMonths = new ArrayList<>(raw.keySet());
        List<Long>   chartCounts = new ArrayList<>(raw.values());
        model.addAttribute("chartMonths", chartMonths);
        model.addAttribute("chartCounts", chartCounts);

        return "dashboard";
    }
}
