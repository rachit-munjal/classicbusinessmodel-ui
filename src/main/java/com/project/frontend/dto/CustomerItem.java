package com.project.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerItem {

    private Integer    customerNumber;
    private String     customerName;
    private String     contactFirstName;
    private String     contactLastName;
    private String     phone;
    private String     addressLine1;
    private String     addressLine2;
    private String     city;
    private String     state;
    private String     postalCode;
    private String     country;
    private BigDecimal creditLimit;
    private Integer    salesRepEmployeeNumber;

    public String getContactFullName() {
        return (contactFirstName != null ? contactFirstName : "") + " "
             + (contactLastName  != null ? contactLastName  : "");
    }

    public String getFormattedCreditLimit() {
        if (creditLimit == null) return "—";
        return NumberFormat.getCurrencyInstance(Locale.US).format(creditLimit);
    }
}
