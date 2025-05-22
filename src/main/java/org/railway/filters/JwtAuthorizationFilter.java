package org.railway.filters;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.railway.config.SecurityProperties;
import org.railway.entity.User;
import org.railway.service.UserService;
import org.railway.service.impl.UserRepository;
import org.railway.utils.JwtUserInfo;
import org.railway.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;
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
            JwtUserInfo UserInfo = JwtUtil.extractUserInfo(accessToken);
            Optional<User> user = userRepository.findByUsername(UserInfo.getUsername());
            authenticateUser(request, UserInfo.getUsername(),user.get().getUserType());
            filterChain.doFilter(request, response);
            return;
        }

        // Access Token 失效，尝试用 Refresh Token 刷新
        if (refreshToken != null && JwtUtil.validateToken(refreshToken)) {
            JwtUserInfo UserInfo = JwtUtil.extractUserInfo(accessToken);
            String newAccessToken = JwtUtil.generateAccessToken(UserInfo.getUsername(),UserInfo.getUserId());

            // 设置新的 Access Token 到 Header
            response.setHeader("Authorization", "Bearer " + newAccessToken);
            Optional<User> user = userRepository.findByUsername(UserInfo.getUsername());
            // 放行并认证用户
            authenticateUser(request, UserInfo.getUsername(),user.get().getUserType());
            filterChain.doFilter(request, response);
            return;
        }
        // 都无效，拒绝访问
        handlerExceptionResolver.resolveException(request, response,
                null,new AuthException("未认证"));

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
