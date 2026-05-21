package com.project.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeItem {

    private Integer      employeeNumber;
    private String       firstName;
    private String       lastName;
    private String       extension;
    private String       email;
    private String       jobTitle;
    private OfficeInfo   office;
    private ManagerInfo  reportsTo;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OfficeInfo {
        private String officeCode;
        private String city;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ManagerInfo {
        private Integer employeeNumber;
        private String  firstName;
        private String  lastName;
    }

    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }

    public String getOfficeCity() {
        return office != null ? office.getCity() : "—";
    }

    public String getManagerDisplay() {
        if (reportsTo == null) return "—";
        return reportsTo.getEmployeeNumber() + " (" + reportsTo.getFirstName() + " " + reportsTo.getLastName() + ")";
    }
}
