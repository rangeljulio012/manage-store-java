package com.manage_store_api.main.Dto;

public class TokenResponseDto {
    private String token;
    private String refreshToken;

    public TokenResponseDto(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() { return token; }
    public String getRefreshToken() { return refreshToken; }
}