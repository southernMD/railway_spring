package org.railway.dto.response;

import lombok.Data;

@Data
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private boolean isAdmin;
    private Long userId;
    private String username;

    public TokenResponse(String accessToken, String refreshToken,boolean isAdmin,Long userId,String username) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isAdmin = isAdmin;
        this.userId = userId;
        this.username = username;
    }
}
