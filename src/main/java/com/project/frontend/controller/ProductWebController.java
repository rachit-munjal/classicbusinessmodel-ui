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
@RequestMapping("/products")
@AllArgsConstructor
public class ProductWebController {

    private final BackendClient backendClient;

    @GetMapping
    public String products(Model model,
                           @RequestParam(defaultValue = "") String search,
                           @RequestParam(defaultValue = "") String line,
                           @RequestParam(defaultValue = "0") int page) {

        PageResponse<ProductItem> products = !line.isBlank()
                ? backendClient.filterProductsByLine(line, page, 10)
                : backendClient.searchProducts(search, page, 10);

        Map<String, Object> stats = backendClient.getProductStats();
        List<String> productLines = new ArrayList<>();
        if (stats.containsKey("productCountPerLine")) {
            Object countPerLine = stats.get("productCountPerLine");
            if (countPerLine instanceof Map<?, ?> map) {
                productLines.addAll(map.keySet().stream().map(Object::toString).sorted().toList());
            }
        }
        model.addAttribute("products",     products);
        model.addAttribute("productLines", productLines);
        model.addAttribute("selectedLine", line);
        model.addAttribute("search",       search);
        model.addAttribute("stats",        stats);
        return "products";
    }

    // ── DETAIL ───────────────────────────────────────────────────────
    @GetMapping("/{code}")
    public String detail(@PathVariable String code, Model model) {
        ProductItem prod = backendClient.getProduct(code);
        if (prod == null) return "redirect:/products";
        model.addAttribute("prod", prod);
        return "product-detail";
    }

    // ── EDIT FORM ────────────────────────────────────────────────────
    @GetMapping("/{code}/edit")
    public String editForm(@PathVariable String code, Model model) {
        ProductItem prod = backendClient.getProduct(code);
        if (prod == null) return "redirect:/products";
        Map<String, Object> stats = backendClient.getProductStats();
        List<String> productLines = new ArrayList<>();
        if (stats.containsKey("productCountPerLine")) {
            Object m = stats.get("productCountPerLine");
            if (m instanceof Map<?, ?> map)
                productLines.addAll(map.keySet().stream().map(Object::toString).sorted().toList());
        }
        model.addAttribute("prod",         prod);
        model.addAttribute("productLines", productLines);
        return "product-edit";
    }

    // ── ADD FORM ─────────────────────────────────────────────────────
    @GetMapping("/add")
    public String addForm(Model model) {
        Map<String, Object> stats = backendClient.getProductStats();
        List<String> productLines = new ArrayList<>();
        if (stats.containsKey("productCountPerLine")) {
            Object m = stats.get("productCountPerLine");
            if (m instanceof Map<?, ?> map)
                productLines.addAll(map.keySet().stream().map(Object::toString).sorted().toList());
        }
        model.addAttribute("productLines", productLines);
        return "product-add";
    }

    @PostMapping("/add")
    public String addSubmit(@RequestParam Map<String, String> form, RedirectAttributes ra) {
        Map<String, Object> data = new HashMap<>(form);
        boolean ok = backendClient.addProduct(data);
        if (ok) {
            ra.addFlashAttribute("flash",     "✓ Product added successfully");
            ra.addFlashAttribute("flashType", "success");
            return "redirect:/products";
        } else {
            ra.addFlashAttribute("flash",     "✗ Failed to add product");
            ra.addFlashAttribute("flashType", "error");
            return "redirect:/products/add";
        }
    }

    // ── QUICK UPDATE (price / qty / msrp) ────────────────────────────
    @PostMapping("/{code}/update")
    public String quickUpdate(@PathVariable String code,
                              @RequestParam(required = false) String buyPrice,
                              @RequestParam(required = false) String qty,
                              @RequestParam(required = false) String msrp,
                              RedirectAttributes ra) {
        try {
            if (buyPrice != null && !buyPrice.isBlank()) backendClient.updateProductPrice(code, buyPrice);
            if (qty      != null && !qty.isBlank())      backendClient.updateProductQty(code, qty);
            if (msrp     != null && !msrp.isBlank())     backendClient.updateProductMsrp(code, msrp);
            ra.addFlashAttribute("flash",     "✓ Product updated successfully");
            ra.addFlashAttribute("flashType", "success");
        } catch (Exception e) {
            ra.addFlashAttribute("flash",     "✗ Update failed");
            ra.addFlashAttribute("flashType", "error");
        }
        return "redirect:/products/" + code;
    }
}
