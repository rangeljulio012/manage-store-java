package com.manage_store_api.main.Dto;

import com.manage_store_api.main.model.Bill;

import java.time.LocalDateTime;

public class BillDto {
    private Long id;
    private String description;
    private Long storeId; // Solo el ID en lugar del objeto completo
    private Float amount;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public BillDto(Bill bill) {
        this.id = bill.getId();
        this.description = bill.getDescription();
        this.storeId = bill.getStore().getId();
        this.amount = bill.getAmount();
        this.updatedAt = bill.getUpdatedAt();
        this.createdAt = bill.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
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
