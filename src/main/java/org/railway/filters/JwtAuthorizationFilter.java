package org.railway.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.railway.config.SecurityProperties;
import org.railway.entity.User;
import org.railway.service.UserService;
import org.railway.service.impl.UserRepository;
import org.railway.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = extractAccessToken(request);
        String refreshToken = extractRefreshToken(request);

        if (accessToken != null && JwtUtil.validateToken(accessToken)) {
            String username = JwtUtil.extractUsername(accessToken);
            Optional<User> user = userRepository.findByUsername(username);
            authenticateUser(request, username,user.get().getUserType());
            filterChain.doFilter(request, response);
            return;
        }

        // Access Token 失效，尝试用 Refresh Token 刷新
        if (refreshToken != null && JwtUtil.validateToken(refreshToken)) {
            String username = JwtUtil.extractUsername(refreshToken);
            String newAccessToken = JwtUtil.generateAccessToken(username);

            // 设置新的 Access Token 到 Header
            response.setHeader("Authorization", "Bearer " + newAccessToken);
            Optional<User> user = userRepository.findByUsername(username);
            // 放行并认证用户
            authenticateUser(request, username,user.get().getUserType());
            filterChain.doFilter(request, response);
            return;
        }

        // 都无效，拒绝访问
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
    }
    private boolean isPublicPath(String path) {
        return SecurityProperties.PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String extractRefreshToken(HttpServletRequest request) {
        return request.getHeader("Refresh-Token");
    }

    //设置用户登录，表示权限列表Collections.emptyList()，可以使用@PreAuthorize("hasRole('USER')")判断是否有ROLE_USER权限
    private void authenticateUser(HttpServletRequest request, String username, Integer userType) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (userType == 1) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username, null, authorities
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
