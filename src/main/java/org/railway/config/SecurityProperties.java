package org.railway.config;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityProperties {

    // 定义不需要 Token 的路径
    public static final List<String> PUBLIC_PATHS = List.of(
            "/auth",
            "/test",
            "/api.html",
            "/swagger-ui",
            "/api-docs"
    );
}
