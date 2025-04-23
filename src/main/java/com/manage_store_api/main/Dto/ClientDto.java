package com.manage_store_api.main.Dto;

import com.manage_store_api.main.model.Client;

import java.time.LocalDateTime;

public class ClientDto {
    private Long id;
    private String name;
    private String address;
    private String document;
    private Long storeId; // Solo el ID en lugar del objeto completo
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public ClientDto(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.address = client.getAddress();
        this.document = client.getDocument();
        this.storeId = client.getStore().getId();
        this.updatedAt = client.getUpdatedAt();
        this.createdAt = client.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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
