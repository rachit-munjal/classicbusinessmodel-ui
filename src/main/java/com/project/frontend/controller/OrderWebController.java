package com.project.frontend.controller;

import com.project.frontend.client.BackendClient;
import com.project.frontend.dto.CustomerItem;
import com.project.frontend.dto.OrderItem;
import com.project.frontend.dto.PageResponse;
import com.project.frontend.dto.ProductItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderWebController {

    private static final List<String> ORDER_STATUSES =
            List.of("Shipped", "In Process", "On Hold", "Cancelled", "Disputed", "Resolved");

    private final BackendClient backendClient;

    // ── LIST ─────────────────────────────────────────────────────────
    @GetMapping
    public String orders(Model model,
                         @RequestParam(defaultValue = "") String search,
                         @RequestParam(defaultValue = "") String status,
                         @RequestParam(defaultValue = "0") int page) {

        PageResponse<OrderItem> ordersPage = null;
        List<OrderItem>         ordersList = null;

        if (!status.isBlank()) {
            ordersList = backendClient.getOrdersByStatus(status);
        } else {
            ordersPage = backendClient.searchOrders(search, page, 10);
        }

        Map<String, Object> stats = backendClient.getOrderStats();
        model.addAttribute("ordersPage",     ordersPage);
        model.addAttribute("ordersList",     ordersList);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("search",         search);
        model.addAttribute("statuses",       ORDER_STATUSES);
        model.addAttribute("stats",          stats);
        return "orders";
    }

    // ── DETAIL ───────────────────────────────────────────────────────
    @GetMapping("/{no}")
    public String detail(@PathVariable Integer no, Model model) {
        OrderItem order = backendClient.getOrder(no);  // ← use the fixed method
        if (order == null) return "redirect:/orders";
        model.addAttribute("order",    order);
        model.addAttribute("statuses", ORDER_STATUSES);
        return "order-detail";
    }

    // ── ADD FORM ─────────────────────────────────────────────────────
    @GetMapping("/add")
    public String addForm(Model model) {
        // Load all customers and all products for the form selectors
        List<CustomerItem> customers = backendClient.searchCustomers("", 0, 200).getContent();
        List<ProductItem>  products  = backendClient.searchProducts("", 0, 200).getContent();
        model.addAttribute("customers",  customers);
        model.addAttribute("products",   products);
        model.addAttribute("statuses",   ORDER_STATUSES);
        model.addAttribute("today",      LocalDate.now().toString());
        return "order-add";
    }

    // ── ADD SUBMIT ────────────────────────────────────────────────────
    @PostMapping("/add")
    public String addSubmit(@RequestParam Map<String, String> form,
                            RedirectAttributes ra) {
        try {
            // Build Order part
            Map<String, Object> order = new LinkedHashMap<>();
            order.put("orderDate",    form.get("orderDate"));
            order.put("requiredDate", form.get("requiredDate"));
            order.put("status",       form.get("status"));
            order.put("comments",     form.getOrDefault("comments", ""));

            // Set customer as nested object matching the entity relationship
            Integer customerNumber = Integer.valueOf(form.get("customerNumber"));
            Map<String, Object> customer = new HashMap<>();
            customer.put("customerNumber", customerNumber);
            order.put("customer", customer);

            // Build OrderDetails list from submitted productCode_X / qty_X pairs
            List<Map<String, Object>> details = new ArrayList<>();
            int lineNumber = 1;
            for (Map.Entry<String, String> entry : form.entrySet()) {
                if (entry.getKey().startsWith("qty_") && !entry.getValue().isBlank()
                        && Integer.parseInt(entry.getValue()) > 0) {
                    String productCode = entry.getKey().substring(4); // strip "qty_"
                    String priceKey    = "price_" + productCode;
                    String priceStr    = form.getOrDefault(priceKey, "0");

                    Map<String, Object> id = new HashMap<>();
                    id.put("orderNumber",  0);   // will be set by OrderService after save
                    id.put("productCode",  productCode);

                    Map<String, Object> detail = new LinkedHashMap<>();
                    detail.put("id",              id);
                    detail.put("quantityOrdered", Integer.valueOf(entry.getValue()));
                    detail.put("priceEach",       new java.math.BigDecimal(priceStr));
                    detail.put("orderLineNumber", (short) lineNumber++);
                    details.add(detail);
                }
            }

            if (details.isEmpty()) {
                ra.addFlashAttribute("flash",     "✗ Please add at least one product to the order");
                ra.addFlashAttribute("flashType", "error");
                return "redirect:/orders/add";
            }

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("order",        order);
            payload.put("orderDetails", details);

            boolean ok = backendClient.createOrder(payload);
            if (ok) {
                ra.addFlashAttribute("flash",     "✓ Order created successfully");
                ra.addFlashAttribute("flashType", "success");
                return "redirect:/orders";
            } else {
                ra.addFlashAttribute("flash",     "✗ Failed to create order");
                ra.addFlashAttribute("flashType", "error");
                return "redirect:/orders/add";
            }
        } catch (Exception e) {
            ra.addFlashAttribute("flash",     "✗ Error: " + e.getMessage());
            ra.addFlashAttribute("flashType", "error");
            return "redirect:/orders/add";
        }
    }

    // ── STATUS UPDATE ─────────────────────────────────────────────────
    @PostMapping("/{no}/status")
    public String updateStatus(@PathVariable Integer no,
                               @RequestParam String status,
                               RedirectAttributes ra) {
        backendClient.updateOrderStatus(no, status);
        ra.addFlashAttribute("flash",     "✓ Order status updated to: " + status);
        ra.addFlashAttribute("flashType", "success");
        return "redirect:/orders/" + no;
    }
}