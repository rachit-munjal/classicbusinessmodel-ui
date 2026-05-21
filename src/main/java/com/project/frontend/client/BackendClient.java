package com.project.frontend.client;

import com.project.frontend.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
public class BackendClient {

    private final RestTemplate restTemplate;

    @Value("${backend.base-url}")
    private String baseUrl;

    public BackendClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // ─────────────────────────────────── DASHBOARD ────────────────────────────────────

    @SuppressWarnings("unchecked")
    public Map<String, Object> getDashboardStats() {
        try {
            return restTemplate.getForObject(baseUrl + "/api/v1/dashboard/stats", Map.class);
        } catch (RestClientException e) {
            log.error("getDashboardStats failed: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    public List<RecentOrder> getRecentOrders() {
        try {
            RecentOrder[] orders = restTemplate.getForObject(
                    baseUrl + "/api/v1/dashboard/recent-orders", RecentOrder[].class);
            return orders != null ? Arrays.asList(orders) : Collections.emptyList();
        } catch (RestClientException e) {
            log.error("getRecentOrders failed: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Long> getOrdersPerMonth() {
        try {
            return restTemplate.getForObject(baseUrl + "/api/v1/dashboard/orders-per-month", Map.class);
        } catch (RestClientException e) {
            log.error("getOrdersPerMonth failed: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    // ─────────────────────────────────── EMPLOYEES ────────────────────────────────────

    public PageResponse<EmployeeItem> searchEmployees(String name, int page, int size) {
        try {
            String url = baseUrl + "/api/v1/employees/search?name=" + encode(name)
                    + "&page=" + page + "&size=" + size;
            ResponseEntity<PageResponse<EmployeeItem>> resp = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<PageResponse<EmployeeItem>>() {});
            return resp.getBody() != null ? resp.getBody() : new PageResponse<>();
        } catch (RestClientException e) {
            log.error("searchEmployees failed: {}", e.getMessage());
            return new PageResponse<>();
        }
    }

    public PageResponse<EmployeeItem> getEmployeesByOffice(String officeCode, int page, int size) {
        try {
            String url = baseUrl + "/api/v1/employees/office/" + encode(officeCode)
                    + "?page=" + page + "&size=" + size;
            ResponseEntity<PageResponse<EmployeeItem>> resp = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<PageResponse<EmployeeItem>>() {});
            return resp.getBody() != null ? resp.getBody() : new PageResponse<>();
        } catch (RestClientException e) {
            log.error("getEmployeesByOffice failed: {}", e.getMessage());
            return new PageResponse<>();
        }
    }

    // ─────────────────────────────────── CUSTOMERS ────────────────────────────────────

    public PageResponse<CustomerItem> searchCustomers(String name, int page, int size) {
        try {
            String url = baseUrl + "/api/v1/customers/search?name=" + encode(name)
                    + "&page=" + page + "&size=" + size;
            ResponseEntity<PageResponse<CustomerItem>> resp = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<PageResponse<CustomerItem>>() {});
            return resp.getBody() != null ? resp.getBody() : new PageResponse<>();
        } catch (RestClientException e) {
            log.error("searchCustomers failed: {}", e.getMessage());
            return new PageResponse<>();
        }
    }

    public PageResponse<CustomerItem> filterCustomersByCountry(String country, int page, int size) {
        try {
            String url = baseUrl + "/api/v1/customers/filter/country?country=" + encode(country)
                    + "&page=" + page + "&size=" + size;
            ResponseEntity<PageResponse<CustomerItem>> resp = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<PageResponse<CustomerItem>>() {});
            return resp.getBody() != null ? resp.getBody() : new PageResponse<>();
        } catch (RestClientException e) {
            log.error("filterCustomersByCountry failed: {}", e.getMessage());
            return new PageResponse<>();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getCustomerStats() {
        try {
            return restTemplate.getForObject(baseUrl + "/api/v1/customers/stats", Map.class);
        } catch (RestClientException e) {
            log.error("getCustomerStats failed: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    // ─────────────────────────────────── ORDERS ───────────────────────────────────────

    public PageResponse<OrderItem> searchOrders(String customerName, int page, int size) {
        try {
            String url = baseUrl + "/api/v1/orders/search?customerName=" + encode(customerName)
                    + "&page=" + page + "&size=" + size;
            ResponseEntity<PageResponse<OrderItem>> resp = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<PageResponse<OrderItem>>() {});
            return resp.getBody() != null ? resp.getBody() : new PageResponse<>();
        } catch (RestClientException e) {
            log.error("searchOrders failed: {}", e.getMessage());
            return new PageResponse<>();
        }
    }

    public List<OrderItem> getOrdersByStatus(String status) {
        try {
            String url = baseUrl + "/api/v1/orders/filter/status?status=" + encode(status);
            OrderItem[] items = restTemplate.getForObject(url, OrderItem[].class);
            return items != null ? Arrays.asList(items) : Collections.emptyList();
        } catch (RestClientException e) {
            log.error("getOrdersByStatus failed: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getOrderStats() {
        try {
            return restTemplate.getForObject(baseUrl + "/api/v1/orders/stats", Map.class);
        } catch (RestClientException e) {
            log.error("getOrderStats failed: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    // ─────────────────────────────────── PRODUCTS ─────────────────────────────────────

    public PageResponse<ProductItem> searchProducts(String name, int page, int size) {
        try {
            String url = baseUrl + "/api/v1/products/search?name=" + encode(name)
                    + "&page=" + page + "&size=" + size;
            ResponseEntity<PageResponse<ProductItem>> resp = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<PageResponse<ProductItem>>() {});
            return resp.getBody() != null ? resp.getBody() : new PageResponse<>();
        } catch (RestClientException e) {
            log.error("searchProducts failed: {}", e.getMessage());
            return new PageResponse<>();
        }
    }

    public PageResponse<ProductItem> filterProductsByLine(String productLine, int page, int size) {
        try {
            String url = baseUrl + "/api/v1/products/filter/line?productLine=" + encode(productLine)
                    + "&page=" + page + "&size=" + size;
            ResponseEntity<PageResponse<ProductItem>> resp = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<PageResponse<ProductItem>>() {});
            return resp.getBody() != null ? resp.getBody() : new PageResponse<>();
        } catch (RestClientException e) {
            log.error("filterProductsByLine failed: {}", e.getMessage());
            return new PageResponse<>();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getProductStats() {
        try {
            return restTemplate.getForObject(baseUrl + "/api/v1/products/stats", Map.class);
        } catch (RestClientException e) {
            log.error("getProductStats failed: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    // ─────────────────────────────────── PAYMENTS ─────────────────────────────────────

    public PageResponse<PaymentItem> getAllPayments(int page, int size) {
        try {
            String url = baseUrl + "/api/v1/payments?page=" + page + "&size=" + size;
            ResponseEntity<PageResponse<PaymentItem>> resp = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<PageResponse<PaymentItem>>() {});
            return resp.getBody() != null ? resp.getBody() : new PageResponse<>();
        } catch (RestClientException e) {
            log.error("getAllPayments failed: {}", e.getMessage());
            return new PageResponse<>();
        }
    }

    public PageResponse<PaymentItem> searchPaymentsByCheck(String checkNumber, int page, int size) {
        try {
            String url = baseUrl + "/api/v1/payments/search/check?checkNumber=" + encode(checkNumber)
                    + "&page=" + page + "&size=" + size;
            ResponseEntity<PageResponse<PaymentItem>> resp = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<PageResponse<PaymentItem>>() {});
            return resp.getBody() != null ? resp.getBody() : new PageResponse<>();
        } catch (RestClientException e) {
            log.error("searchPaymentsByCheck failed: {}", e.getMessage());
            return new PageResponse<>();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getPaymentStats() {
        try {
            return restTemplate.getForObject(baseUrl + "/api/v1/payments/stats", Map.class);
        } catch (RestClientException e) {
            log.error("getPaymentStats failed: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    // ─────────────────────────────────── OFFICES ──────────────────────────────────────

    public List<OfficeItem> getAllOffices() {
        try {
            OfficeItem[] offices = restTemplate.getForObject(
                    baseUrl + "/api/v1/offices/all", OfficeItem[].class);
            return offices != null ? Arrays.asList(offices) : Collections.emptyList();
        } catch (RestClientException e) {
            log.error("getAllOffices failed: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public OfficeItem getOffice(String code) {
        try {
            return restTemplate.getForObject(
                    baseUrl + "/api/v1/offices/" + encode(code), OfficeItem.class);
        } catch (RestClientException e) {
            log.error("getOffice({}) failed: {}", code, e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getOfficeStats() {
        try {
            return restTemplate.getForObject(baseUrl + "/api/v1/offices/stats", Map.class);
        } catch (RestClientException e) {
            log.error("getOfficeStats failed: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    public List<EmployeeItem> getOfficeEmployees(String officeCode) {
        try {
            ResponseEntity<PageResponse<EmployeeItem>> resp = restTemplate.exchange(
                    baseUrl + "/api/v1/offices/" + encode(officeCode) + "/employees?size=100",
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<PageResponse<EmployeeItem>>() {});
            PageResponse<EmployeeItem> page = resp.getBody();
            return page != null ? page.getContent() : Collections.emptyList();
        } catch (RestClientException e) {
            log.error("getOfficeEmployees failed: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean addOffice(Map<String, ?> data) {
        try {
            restTemplate.postForObject(baseUrl + "/api/v1/offices/add", data, Map.class);
            return true;
        } catch (RestClientException e) {
            log.error("addOffice failed: {}", e.getMessage());
            return false;
        }
    }

    public void updateOfficePhone(String officeCode, String phone) {
        try {
            restTemplate.put(baseUrl + "/api/v1/offices/" + encode(officeCode)
                    + "/phone?phone=" + encode(phone), null);
        } catch (RestClientException e) {
            log.error("updateOfficePhone failed: {}", e.getMessage());
        }
    }

    // ─────────────────────────────────── EMPLOYEE CRUD ────────────────────────────────────

    public EmployeeItem getEmployee(Integer no) {
        try {
            return restTemplate.getForObject(
                    baseUrl + "/api/v1/employees/" + no, EmployeeItem.class);
        } catch (RestClientException e) {
            log.error("getEmployee({}) failed: {}", no, e.getMessage());
            return null;
        }
    }

    public List<EmployeeItem> getEmployeeReportees(Integer no) {
        try {
            EmployeeItem[] arr = restTemplate.getForObject(
                    baseUrl + "/api/v1/employees/" + no + "/reportees", EmployeeItem[].class);
            return arr != null ? Arrays.asList(arr) : Collections.emptyList();
        } catch (RestClientException e) {
            log.error("getEmployeeReportees failed: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<CustomerItem> getEmployeeCustomers(Integer no) {
        try {
            ResponseEntity<PageResponse<CustomerItem>> resp = restTemplate.exchange(
                    baseUrl + "/api/v1/employees/" + no + "/customers?size=50",
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<PageResponse<CustomerItem>>() {});
            return resp.getBody() != null ? resp.getBody().getContent() : Collections.emptyList();
        } catch (RestClientException e) {
            log.error("getEmployeeCustomers failed: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean addEmployee(Map<String, Object> data) {
        try {
            restTemplate.postForObject(baseUrl + "/api/v1/employees/add", data, Map.class);
            return true;
        } catch (RestClientException e) {
            log.error("addEmployee failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean updateEmployee(Integer no, Map<String, Object> data) {
        try {
            restTemplate.put(baseUrl + "/api/v1/employees/update/" + no, data);
            return true;
        } catch (RestClientException e) {
            log.error("updateEmployee({}) failed: {}", no, e.getMessage());
            return false;
        }
    }

    public boolean deleteEmployee(Integer no) {
        try {
            restTemplate.delete(baseUrl + "/api/v1/employees/delete/" + no);
            return true;
        } catch (RestClientException e) {
            log.error("deleteEmployee({}) failed: {}", no, e.getMessage());
            return false;
        }
    }

    // ─────────────────────────────────── CUSTOMER CRUD ────────────────────────────────────

    public CustomerItem getCustomer(Integer no) {
        try {
            return restTemplate.getForObject(
                    baseUrl + "/api/v1/customers/" + no, CustomerItem.class);
        } catch (RestClientException e) {
            log.error("getCustomer({}) failed: {}", no, e.getMessage());
            return null;
        }
    }

    public List<OrderItem> getCustomerOrders(Integer no) {
        try {
            OrderItem[] arr = restTemplate.getForObject(
                    baseUrl + "/api/v1/customers/" + no + "/orders", OrderItem[].class);
            return arr != null ? Arrays.asList(arr) : Collections.emptyList();
        } catch (RestClientException e) {
            log.error("getCustomerOrders failed: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<PaymentItem> getCustomerPayments(Integer no) {
        try {
            PaymentItem[] arr = restTemplate.getForObject(
                    baseUrl + "/api/v1/customers/" + no + "/payments", PaymentItem[].class);
            return arr != null ? Arrays.asList(arr) : Collections.emptyList();
        } catch (RestClientException e) {
            log.error("getCustomerPayments failed: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean addCustomer(Map<String, Object> data) {
        try {
            restTemplate.postForObject(baseUrl + "/api/v1/customers/add", data, Map.class);
            return true;
        } catch (RestClientException e) {
            log.error("addCustomer failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean updateCustomer(Integer no, Map<String, Object> data) {
        try {
            restTemplate.put(baseUrl + "/api/v1/customers/update/" + no, data);
            return true;
        } catch (RestClientException e) {
            log.error("updateCustomer({}) failed: {}", no, e.getMessage());
            return false;
        }
    }

    // ─────────────────────────────────── PRODUCT CRUD ────────────────────────────────────

    public ProductItem getProduct(String code) {
        try {
            return restTemplate.getForObject(
                    baseUrl + "/api/v1/products/" + encode(code), ProductItem.class);
        } catch (RestClientException e) {
            log.error("getProduct({}) failed: {}", code, e.getMessage());
            return null;
        }
    }

    public boolean addProduct(Map<String, Object> data) {
        try {
            restTemplate.postForObject(baseUrl + "/api/v1/products/add", data, Map.class);
            return true;
        } catch (RestClientException e) {
            log.error("addProduct failed: {}", e.getMessage());
            return false;
        }
    }

    public void updateProductPrice(String code, String price) {
        try {
            restTemplate.put(baseUrl + "/api/v1/products/" + encode(code)
                    + "/price/" + encode(price), null);
        } catch (RestClientException e) {
            log.error("updateProductPrice failed: {}", e.getMessage());
        }
    }

    public void updateProductMsrp(String code, String msrp) {
        try {
            restTemplate.put(baseUrl + "/api/v1/products/" + encode(code)
                    + "/msrp/" + encode(msrp), null);
        } catch (RestClientException e) {
            log.error("updateProductMsrp failed: {}", e.getMessage());
        }
    }

    public void updateProductQty(String code, String qty) {
        try {
            restTemplate.put(baseUrl + "/api/v1/products/" + encode(code)
                    + "/quantity/" + encode(qty), null);
        } catch (RestClientException e) {
            log.error("updateProductQty failed: {}", e.getMessage());
        }
    }

    // ─────────────────────────────────── ORDER CRUD ───────────────────────────────────────

    public OrderItem getOrder(Integer no) {
        try {
            return restTemplate.getForObject(
                    baseUrl + "/api/v1/orders/" + no, OrderItem.class);
        } catch (RestClientException e) {
            log.error("getOrder({}) failed: {}", no, e.getMessage());
            return null;
        }
    }

    public boolean createOrder(Map<String, Object> payload) {
        try {
            restTemplate.postForObject(baseUrl + "/api/v1/orders/create", payload, Map.class);
            return true;
        } catch (RestClientException e) {
            log.error("createOrder failed: {}", e.getMessage());
            return false;
        }
    }

    public void updateOrderStatus(Integer no, String status) {
        try {
            restTemplate.put(baseUrl + "/api/v1/orders/" + no
                    + "/status?status=" + encode(status), null);
        } catch (RestClientException e) {
            log.error("updateOrderStatus failed: {}", e.getMessage());
        }
    }

    // ─────────────────────────────────── PAYMENT CRUD ─────────────────────────────────────
    public PaymentItem getPayment(Integer custNo, String checkNo) {
        try {
            return restTemplate.getForObject(
                    baseUrl + "/api/v1/payments/" + custNo + "/" + encode(checkNo),
                    PaymentItem.class);
        } catch (RestClientException e) {
            log.error("getPayment({}/{}) failed: {}", custNo, checkNo, e.getMessage());
            return null;
        }
    }


    public void updatePaymentAmount(Integer custNo, String checkNo, String amount) {
        try {
            restTemplate.put(baseUrl + "/api/v1/payments/" + custNo
                    + "/" + encode(checkNo) + "/amount?amount=" + encode(amount), null);
        } catch (RestClientException e) {
            log.error("updatePaymentAmount failed: {}", e.getMessage());
        }
    }

    // ─────────────────────────────────── HELPERS ──────────────────────────────────────

    private String encode(String value) {
        if (value == null) return "";
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }
}