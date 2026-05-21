package com.project.frontend.controller;

import com.project.frontend.client.BackendClient;
import com.project.frontend.dto.PageResponse;
import com.project.frontend.dto.PaymentItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentWebController {

    private final BackendClient backendClient;

    // ── LIST ──────────────────────────────────────────────────────────
    @GetMapping
    public String payments(Model model,
                           @RequestParam(defaultValue = "") String search,
                           @RequestParam(defaultValue = "0") int page) {
        PageResponse<PaymentItem> payments = search.isBlank()
                ? backendClient.getAllPayments(page, 10)
                : backendClient.searchPaymentsByCheck(search, page, 10);
        Map<String, Object> stats = backendClient.getPaymentStats();
        model.addAttribute("payments", payments);
        model.addAttribute("search",   search);
        model.addAttribute("stats",    stats);
        return "payments";
    }

    // ── DETAIL ────────────────────────────────────────────────────────
    @GetMapping("/{custNo}/{checkNo}")
    public String detail(@PathVariable Integer custNo,
                         @PathVariable String checkNo,
                         Model model) {
        PaymentItem payment = backendClient.getPayment(custNo, checkNo);
        if (payment == null) return "redirect:/payments";
        model.addAttribute("payment", payment);
        return "payment-detail";
    }

    // ── AMOUNT UPDATE ─────────────────────────────────────────────────
    @PostMapping("/{custNo}/{checkNo}/update")
    public String updateAmount(@PathVariable Integer custNo,
                               @PathVariable String checkNo,
                               @RequestParam String amount,
                               RedirectAttributes ra) {
        backendClient.updatePaymentAmount(custNo, checkNo, amount);
        ra.addFlashAttribute("flash",     "✓ Payment amount updated");
        ra.addFlashAttribute("flashType", "success");
        return "redirect:/payments/" + custNo + "/" + checkNo;
    }
}