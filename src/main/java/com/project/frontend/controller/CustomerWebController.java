package com.project.frontend.controller;

import com.project.frontend.client.BackendClient;
import com.project.frontend.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerWebController {

    private final BackendClient backendClient;

    // ── LIST ─────────────────────────────────────────────────────────
    @GetMapping
    public String customers(Model model,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String country,
            @RequestParam(defaultValue = "0") int page) {

        PageResponse<CustomerItem> customers = !country.isBlank()
                ? backendClient.filterCustomersByCountry(country, page, 10)
                : backendClient.searchCustomers(search, page, 10);

        Map<String, Object> stats = backendClient.getCustomerStats();
        model.addAttribute("customers",       customers);
        model.addAttribute("selectedCountry", country);
        model.addAttribute("search",          search);
        model.addAttribute("stats",           stats);
        return "customers";
    }

    // ── DETAIL ───────────────────────────────────────────────────────
    @GetMapping("/{no}")
    public String detail(@PathVariable Integer no, Model model) {
        CustomerItem cust = backendClient.getCustomer(no);
        if (cust == null) return "redirect:/customers";
        List<OrderItem>   orders   = backendClient.getCustomerOrders(no);
        List<PaymentItem> payments = backendClient.getCustomerPayments(no);
        model.addAttribute("cust",     cust);
        model.addAttribute("orders",   orders);
        model.addAttribute("payments", payments);
        return "customer-detail";
    }

    // ── ADD FORM ─────────────────────────────────────────────────────
    @GetMapping("/add")
    public String addForm(Model model) {
        List<EmployeeItem> employees = backendClient.searchEmployees("", 0, 100).getContent();
        model.addAttribute("employees", employees);
        return "customer-add";
    }

    @PostMapping("/add")
    public String addSubmit(@RequestParam Map<String, String> form, RedirectAttributes ra) {
        Map<String, Object> data = new HashMap<>(form);
        boolean ok = backendClient.addCustomer(data);
        if (ok) {
            ra.addFlashAttribute("flash",     "✓ Customer added successfully");
            ra.addFlashAttribute("flashType", "success");
            return "redirect:/customers";
        } else {
            ra.addFlashAttribute("flash",     "✗ Failed to add customer");
            ra.addFlashAttribute("flashType", "error");
            return "redirect:/customers/add";
        }
    }

    // ── EDIT FORM ────────────────────────────────────────────────────
    @GetMapping("/{no}/edit")
    public String editForm(@PathVariable Integer no, Model model) {
        CustomerItem cust = backendClient.getCustomer(no);
        if (cust == null) return "redirect:/customers";
        List<EmployeeItem> employees = backendClient.searchEmployees("", 0, 100).getContent();
        model.addAttribute("cust",      cust);
        model.addAttribute("employees", employees);
        return "customer-edit";
    }

    @PostMapping("/{no}/edit")
    public String editSubmit(@PathVariable Integer no,
                              @RequestParam Map<String, String> form,
                              RedirectAttributes ra) {
        Map<String, Object> data = new HashMap<>(form);
        boolean ok = backendClient.updateCustomer(no, data);
        if (ok) {
            ra.addFlashAttribute("flash",     "✓ Customer updated successfully");
            ra.addFlashAttribute("flashType", "success");
        } else {
            ra.addFlashAttribute("flash",     "✗ Update failed");
            ra.addFlashAttribute("flashType", "error");
        }
        return "redirect:/customers/" + no;
    }
}
