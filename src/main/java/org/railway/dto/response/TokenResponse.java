package org.railway.dto.response;

import lombok.Data;

@Data
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private boolean isAdmin;
    private Long userId;
    private String username;
    private String email;

    public TokenResponse(String accessToken, String refreshToken,boolean isAdmin,Long userId,String username,String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isAdmin = isAdmin;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}
