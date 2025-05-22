package org.railway.utils;

import lombok.Data;

@Data

public class JwtUserInfo {
    private Long userId;
    private String username;

    public JwtUserInfo(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

}
