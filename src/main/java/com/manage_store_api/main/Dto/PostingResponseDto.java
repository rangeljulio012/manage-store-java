package com.manage_store_api.main.Dto;

public class PostingResponseDto {
    private Object data;
    private String message;

    // Constructor
    public PostingResponseDto(Object data, String message) {
        this.data = data;
        this.message = message;
    }

    // Getters (necesarios para la serialización JSON)
    public Object getData() { return data; }
    public String getMessage() { return message; }
}