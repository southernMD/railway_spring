package org.railway.config;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.railway.filters.JwtAuthorizationFilter;
import org.railway.filters.UserIdCheckFilter;
import org.railway.service.impl.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class Security implements HandlerExceptionResolver {

    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;
    public Security(UserRepository userRepository, HandlerExceptionResolver handlerExceptionResolver) {
        this.userRepository = userRepository;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/api.html").permitAll()
                        .requestMatchers("/auth/**").permitAll()  // 放行 /auth 及其所有子路径
                        .requestMatchers("/test").permitAll()     // 放行 /test 接口
                        .anyRequest().authenticated()             // 其他接口都需要认证
                )
                .addFilterBefore(new JwtAuthorizationFilter(userRepository,handlerExceptionResolver), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new UserIdCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        return null;
    }
}
