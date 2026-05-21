package com.project.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentItem {

    private PaymentId  id;
    private Integer    orderNumber;
    private String     paymentDate;
    private BigDecimal amount;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PaymentId {
        private String  checkNumber;
        private Integer customerNumber;
    }

    public String getCheckNumber() {
        return id != null ? id.getCheckNumber() : "—";
    }

    public Integer getCustomerNumber() {
        return id != null ? id.getCustomerNumber() : null;
    }

    public String getFormattedAmount() {
        if (amount == null) return "—";
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }
}
