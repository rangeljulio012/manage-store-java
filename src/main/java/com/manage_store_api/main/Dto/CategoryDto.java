package com.manage_store_api.main.Dto;


import com.manage_store_api.main.model.Category;

public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private Long storeId; // Solo el ID en lugar del objeto completo

    // Constructor que acepta una entidad Category
    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.storeId = category.getStore().getId(); // Accede al ID directamente
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

}