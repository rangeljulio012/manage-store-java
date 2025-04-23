package com.manage_store_api.main.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manage_store_api.main.Utils.Enums.SalesStatus;
import com.manage_store_api.main.Utils.Enums.SalespPaymentMethod;
import com.manage_store_api.main.Utils.SaleItem;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Float total;

    @Column(nullable = false)
    private Float appliedRate;

    @Column(nullable = false)
    private Float oficialRate;

    @Column(nullable = false)
    private SalesStatus status;

    @Column(nullable = false)
    private SalespPaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = true)
    private String paymentReference;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<SaleItem> items;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Getters y setters
    public List<SaleItem> getItems() {
        return items;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items;
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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

}