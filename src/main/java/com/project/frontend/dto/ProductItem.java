package com.project.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductItem {

    private String     productCode;
    private String     productName;
    private String     productScale;
    private String     productVendor;
    private String     productDescription;
    private Short      quantityInStock;
    private BigDecimal buyPrice;
    private BigDecimal msrp;

    /** Flat field returned by GET /{code} custom endpoint. */
    private String     productLineName;

    /** Nested field returned by list projections. */
    private ProductLineInfo productLine;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductLineInfo {
        private String productLine;
    }

    /** Returns product line name from either the flat or nested field. */
    public String getProductLineName() {
        if (productLineName != null) return productLineName;
        return productLine != null ? productLine.getProductLine() : "—";
    }

    public String getFormattedBuyPrice() {
        if (buyPrice == null) return "—";
        return NumberFormat.getCurrencyInstance(Locale.US).format(buyPrice);
    }

    public String getFormattedMsrp() {
        if (msrp == null) return "—";
        return NumberFormat.getCurrencyInstance(Locale.US).format(msrp);
    }
}
