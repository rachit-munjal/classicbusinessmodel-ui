package com.project.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecentOrder {

    private Integer    orderNumber;
    private String     orderDate;
    private String     status;
    private BigDecimal orderTotal;
    private CustomerInfo customer;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerInfo {
        private Integer customerNumber;
        private String  customerName;
    }

    /** Returns a CSS-safe badge class from the status string, e.g. "In Process" → "in-process". */
    public String getStatusClass() {
        if (status == null) return "unknown";
        return status.toLowerCase().replace(" ", "-");
    }
}
