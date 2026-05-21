package com.project.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses the Spring Data REST HAL response from GET /offices.
 * Shape: { "_embedded": { "offices": [...] }, "page": { ... } }
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfficesHalResponse {

    @JsonProperty("_embedded")
    private Embedded embedded;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Embedded {
        private List<OfficeItem> offices = new ArrayList<>();
    }

    public List<OfficeItem> getOffices() {
        if (embedded == null || embedded.getOffices() == null) return new ArrayList<>();
        return embedded.getOffices();
    }
}
