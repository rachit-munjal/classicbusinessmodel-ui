package com.project.frontend.controller;

import com.project.frontend.client.BackendClient;
import com.project.frontend.dto.EmployeeItem;
import com.project.frontend.dto.OfficeItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/offices")
@AllArgsConstructor
public class OfficeWebController {

    private final BackendClient backendClient;

    // ── LIST ──────────────────────────────────────────────────────────
    @GetMapping
    public String offices(Model model) {
        List<OfficeItem>    offices = backendClient.getAllOffices();
        Map<String, Object> stats   = backendClient.getOfficeStats();
        model.addAttribute("offices", offices);
        model.addAttribute("stats",   stats);
        return "offices";
    }

    // ── DETAIL ───────────────────────────────────────────────────────
    @GetMapping("/{code}")
    public String detail(@PathVariable String code, Model model) {
        OfficeItem office = backendClient.getOffice(code);
        if (office == null) return "redirect:/offices";
        List<EmployeeItem> employees = backendClient.getOfficeEmployees(code);
        model.addAttribute("office",    office);
        model.addAttribute("employees", employees);
        return "office-detail";
    }

    // ── ADD FORM ──────────────────────────────────────────────────────
    @GetMapping("/add")
    public String addForm() {
        return "office-add";
    }

    // ── ADD SUBMIT ────────────────────────────────────────────────────
    @PostMapping("/add")
    public String addSubmit(@RequestParam Map<String, String> form,
                            RedirectAttributes ra) {
        boolean ok = backendClient.addOffice(form);
        if (ok) {
            ra.addFlashAttribute("flash",     "✓ Office created successfully");
            ra.addFlashAttribute("flashType", "success");
            return "redirect:/offices/" + form.get("officeCode");
        }
        ra.addFlashAttribute("flash",     "✗ Failed to create office. Code may already exist.");
        ra.addFlashAttribute("flashType", "error");
        return "redirect:/offices/add";
    }

    // ── UPDATE PHONE ──────────────────────────────────────────────────
    @PostMapping("/{code}/phone")
    public String updatePhone(@PathVariable String code,
                              @RequestParam String phone,
                              RedirectAttributes ra) {
        backendClient.updateOfficePhone(code, phone);
        ra.addFlashAttribute("flash",     "✓ Office phone updated");
        ra.addFlashAttribute("flashType", "success");
        return "redirect:/offices/" + code;
    }
}