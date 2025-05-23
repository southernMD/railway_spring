package org.railway.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 生成安全的密钥
    private static final long ACCESS_TOKEN_EXPIRATION = 600_0 * 20; // 10 分钟
    private static final long REFRESH_TOKEN_EXPIRATION = 2_592_000_000L; // 30 天

    // 生成 Access Token
    public static String generateAccessToken(String username, Long userId) {
        return Jwts.builder()
                .setSubject(username) // 设置主题为用户名
                .claim("userId", userId) // 添加自定义字段 userId
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION)) // 设置过期时间
                .signWith(SECRET_KEY) // 使用密钥签名
                .compact(); // 生成紧凑的 JWT 字符串
    }


    // 生成 Refresh Token
    public static String generateRefreshToken(String username, Long userId) {
        return Jwts.builder()
                .setSubject(username) // 设置主题为用户名
                .claim("userId", userId) // 添加自定义字段 userId
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION)) // 设置过期时间
                .signWith(SECRET_KEY) // 使用密钥签名
                .compact(); // 生成紧凑的 JWT 字符串
    }

    // 解析 Token，提取用户 ID
    public static JwtUserInfo extractUserInfo(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject(); // 提取用户名
        Long userId = claims.get("userId", Long.class); // 提取用户 ID
        return new JwtUserInfo(userId, username); // 返回 JwtUserInfo 对象
    }
    // 验证 Token
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
