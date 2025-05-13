package org.railway.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.EmailLoginRequest;
import org.railway.dto.request.UsernameLoginRequest;
import org.railway.dto.response.AccessTokenResponse;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.TokenResponse;
import org.railway.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    // 使用用户名登录
    @PostMapping("/login/username")
    public BaseResponse<TokenResponse> loginByUsername(@Valid @RequestBody UsernameLoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessToken = JwtUtil.generateAccessToken(auth.getName());
        String refreshToken = JwtUtil.generateRefreshToken(auth.getName());

        return BaseResponse.success(new TokenResponse(accessToken, refreshToken));
    }

    // 使用邮箱登录
    @PostMapping("/login/email")
    public BaseResponse<TokenResponse> loginByEmail(@Valid @RequestBody EmailLoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessToken = JwtUtil.generateAccessToken(auth.getName());
        String refreshToken = JwtUtil.generateRefreshToken(auth.getName());
        return BaseResponse.success(new TokenResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public BaseResponse<AccessTokenResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = request.getHeader("Authorization");
        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
            if (JwtUtil.validateToken(refreshToken)) {
                String username = JwtUtil.extractUsername(refreshToken);
                String accessToken = JwtUtil.generateAccessToken(username);
                return BaseResponse.success(new AccessTokenResponse(accessToken));
            }
        }

        throw new AccessDeniedException("Invalid refresh token");
    }
}
