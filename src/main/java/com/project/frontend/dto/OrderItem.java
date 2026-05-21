package com.project.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItem {

    private Integer      orderNumber;
    private String       orderDate;
    private String       requiredDate;
    private String       shippedDate;
    private String       status;
    private CustomerInfo customer;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerInfo {
        private Integer customerNumber;
        private String  customerName;
    }

    public String getCustomerName() {
        return customer != null ? customer.getCustomerName() : "—";
    }

    /** CSS-safe class name from status string. */
    public String getStatusClass() {
        if (status == null) return "unknown";
        return status.toLowerCase().replace(" ", "-");
    }
}
