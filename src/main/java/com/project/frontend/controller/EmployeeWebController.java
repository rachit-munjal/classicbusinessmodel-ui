package com.project.frontend.controller;

import com.project.frontend.client.BackendClient;
import com.project.frontend.dto.CustomerItem;
import com.project.frontend.dto.EmployeeItem;
import com.project.frontend.dto.OfficeItem;
import com.project.frontend.dto.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/employees")
@AllArgsConstructor
public class EmployeeWebController {

    private final BackendClient backendClient;

    // ── LIST ─────────────────────────────────────────────────────────
    @GetMapping
    public String employees(Model model,
                            @RequestParam(defaultValue = "") String search,
                            @RequestParam(defaultValue = "") String office,
                            @RequestParam(defaultValue = "0") int page) {

        PageResponse<EmployeeItem> employees = !office.isBlank()
                ? backendClient.getEmployeesByOffice(office, page, 10)
                : backendClient.searchEmployees(search, page, 10);

        List<OfficeItem> offices = backendClient.getAllOffices();
        Map<String, Object> stats = backendClient.getOfficeStats();

        model.addAttribute("employees",      employees);
        model.addAttribute("offices",        offices);
        model.addAttribute("selectedOffice", office);
        model.addAttribute("search",         search);
        model.addAttribute("stats",          stats);
        return "employees";
    }

    // ── DETAIL ───────────────────────────────────────────────────────
    @GetMapping("/{no}")
    public String detail(@PathVariable Integer no, Model model) {
        EmployeeItem emp = backendClient.getEmployee(no);
        if (emp == null) return "redirect:/employees";
        List<EmployeeItem> reportees = backendClient.getEmployeeReportees(no);
        List<CustomerItem> customers = backendClient.getEmployeeCustomers(no);
        model.addAttribute("emp",       emp);
        model.addAttribute("reportees", reportees);
        model.addAttribute("customers", customers);
        return "employee-detail";
    }

    // ── ADD FORM ─────────────────────────────────────────────────────
    @GetMapping("/add")
    public String addForm(Model model) {
        List<OfficeItem>   offices   = backendClient.getAllOffices();
        List<EmployeeItem> managers  = backendClient.searchEmployees("", 0, 100).getContent();
        model.addAttribute("offices",  offices);
        model.addAttribute("managers", managers);
        return "employee-add";
    }

    @PostMapping("/add")
    public String addSubmit(@RequestParam Map<String, String> form,
                            RedirectAttributes ra) {
        Map<String, Object> data = new HashMap<>(form);
        boolean ok = backendClient.addEmployee(data);
        if (ok) {
            ra.addFlashAttribute("flash",     "✓ Employee added successfully");
            ra.addFlashAttribute("flashType", "success");
            return "redirect:/employees";
        } else {
            ra.addFlashAttribute("flash",     "✗ Failed to add employee");
            ra.addFlashAttribute("flashType", "error");
            return "redirect:/employees/add";
        }
    }

    // ── EDIT FORM ────────────────────────────────────────────────────
    @GetMapping("/{no}/edit")
    public String editForm(@PathVariable Integer no, Model model) {
        EmployeeItem emp = backendClient.getEmployee(no);
        if (emp == null) return "redirect:/employees";
        List<OfficeItem>   offices  = backendClient.getAllOffices();
        List<EmployeeItem> managers = backendClient.searchEmployees("", 0, 100).getContent();
        model.addAttribute("emp",      emp);
        model.addAttribute("offices",  offices);
        model.addAttribute("managers", managers);
        return "employee-edit";
    }

    @PostMapping("/{no}/edit")
    public String editSubmit(@PathVariable Integer no,
                             @RequestParam Map<String, String> form,
                             RedirectAttributes ra) {
        Map<String, Object> data = new HashMap<>(form);
        boolean ok = backendClient.updateEmployee(no, data);
        if (ok) {
            ra.addFlashAttribute("flash",     "✓ Employee updated successfully");
            ra.addFlashAttribute("flashType", "success");
        } else {
            ra.addFlashAttribute("flash",     "✗ Update failed");
            ra.addFlashAttribute("flashType", "error");
        }
        return "redirect:/employees/" + no;
    }

    // ── DELETE ───────────────────────────────────────────────────────
    @PostMapping("/{no}/delete")
    public String delete(@PathVariable Integer no, RedirectAttributes ra) {
        boolean ok = backendClient.deleteEmployee(no);
        if (ok) {
            ra.addFlashAttribute("flash",     "✓ Employee deleted");
            ra.addFlashAttribute("flashType", "success");
        } else {
            ra.addFlashAttribute("flash",     "✗ Delete failed");
            ra.addFlashAttribute("flashType", "error");
        }
        return "redirect:/employees";
    }
}
