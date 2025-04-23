package com.manage_store_api.main.Dto;
import java.util.List;

public class RestResponseDto {
    private List data;
    private String message;

    // Constructor
    public RestResponseDto(List data, String message) {
        this.data = data;
        this.message = message;
    }

    // Getters (necesarios para la serialización JSON)
    public List getData() { return data; }
    public String getMessage() { return message; }
}