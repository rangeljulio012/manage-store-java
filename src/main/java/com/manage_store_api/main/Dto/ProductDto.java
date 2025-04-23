package com.manage_store_api.main.Dto;


import com.manage_store_api.main.model.Product;

public class ProductDto {
    private Long id;
    private String name;
    private String picture;
    private Float quantity;
    private Float price;
    private Float buyPrice;
    private Long storeId;
    private Long categoryId;

    // Constructor que acepta una entidad Category
    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.picture = product.getPicture();
        this.quantity = product.getQuantity();
        this.price = product.getPrice();
        this.buyPrice = product.getBuyPrice();
        this.storeId = product.getStore().getId(); // Accede al ID directamente
        this.categoryId = product.getCategory().getId(); // Accede al ID directamente
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Float buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}