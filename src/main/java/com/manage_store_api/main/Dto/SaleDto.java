package com.manage_store_api.main.Dto;

import com.manage_store_api.main.Utils.Enums.SalesStatus;
import com.manage_store_api.main.Utils.Enums.SalespPaymentMethod;
import com.manage_store_api.main.Utils.SaleItem;
import com.manage_store_api.main.model.Sale;

import java.time.LocalDateTime;
import java.util.List;

public class SaleDto {
    private Long id;
    private String address;
    private Float total;
    private Float appliedRate;
    private Float oficialRate;
    private SalesStatus status;
    private SalespPaymentMethod paymentMethod;
    private Long storeId;
    private Long clientId;
    private String paymentReference;
    private List<SaleItem> items;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;



    public SaleDto(Sale sale) {
        this.id = sale.getId();
        this.address = sale.getAddress();
        this.total = sale.getTotal();
        this.appliedRate = sale.getAppliedRate();
        this.oficialRate = sale.getOficialRate();
        this.status = sale.getStatus();
        this.paymentMethod = sale.getPaymentMethod();
        this.storeId = sale.getStore().getId();
        this.clientId = sale.getClient().getId();
        this.paymentReference = sale.getPaymentReference();
        this.items = sale.getItems();
        this.updatedAt = sale.getUpdatedAt();
        this.createdAt = sale.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Float getAppliedRate() {
        return appliedRate;
    }

    public void setAppliedRate(Float appliedRate) {
        this.appliedRate = appliedRate;
    }

    public Float getOficialRate() {
        return oficialRate;
    }

    public void setOficialRate(Float oficialRate) {
        this.oficialRate = oficialRate;
    }

    public SalesStatus getStatus() {
        return status;
    }

    public void setStatus(SalesStatus status) {
        this.status = status;
    }

    public SalespPaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(SalespPaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
