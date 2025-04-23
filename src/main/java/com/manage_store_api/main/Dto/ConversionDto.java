package com.manage_store_api.main.Dto;


import com.manage_store_api.main.model.Conversion;

import java.time.LocalDateTime;

public class ConversionDto {
    private Float rateApplied;
    private Float oficialRate;
    private boolean isCurrentDate;
    private Long storeId; // Solo el ID en lugar del objeto completo
    private LocalDateTime untilDate;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public ConversionDto(Conversion conversion) {
        this.rateApplied = conversion.getRateApplied();
        this.oficialRate = conversion.getOficialRate();
        this.isCurrentDate = conversion.isCurrentDate();
        this.storeId = conversion.getStore().getId();
        this.untilDate = conversion.getUntilDate();
        this.updatedAt = conversion.getUpdatedAt();
        this.createdAt = conversion.getCreatedAt();
    }

    public Float getRateApplied() {
        return rateApplied;
    }

    public void setRateApplied(Float rateApplied) {
        this.rateApplied = rateApplied;
    }

    public Float getOficialRate() {
        return oficialRate;
    }

    public void setOficialRate(Float oficialRate) {
        this.oficialRate = oficialRate;
    }

    public boolean isCurrentDate() {
        return isCurrentDate;
    }

    public void setCurrentDate(boolean currentDate) {
        isCurrentDate = currentDate;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public LocalDateTime getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(LocalDateTime untilDate) {
        this.untilDate = untilDate;
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