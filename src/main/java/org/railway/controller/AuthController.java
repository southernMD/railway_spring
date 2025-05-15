package org.railway.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.EmailLoginRequest;
import org.railway.dto.request.RegisterRequest;
import org.railway.dto.request.UsernameLoginRequest;
import org.railway.dto.response.AccessTokenResponse;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.TokenResponse;
import org.railway.entity.User;
import org.railway.entity.VerificationCode;
import org.railway.service.UserService;
import org.railway.utils.GetRandomNumber;
import org.railway.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import org.railway.utils.Email;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final Email emailUtil ;
    private final TransactionTemplate template;

    // 生成并发送验证码的接口
    @RequestMapping(value = "/send-code", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResponse<String> sendVerificationCode(@RequestParam String email) {
        final String code = GetRandomNumber.generateVerificationCode(6);
        // 2. 设置过期时间为当前时间 + 5 分钟
        final LocalDateTime expireTime = LocalDateTime.now().plusMinutes(5);

        template.execute(status -> {
            userService.updateCodeAndExpireTime(email, code, expireTime);
            return null;
        });


        String subject = "注册验证码";
        String htmlContent =
        """
            <div style='display: flex; flex-direction: column; color:#222;'>
              <div>你好：</div>
              <div>你的验证码是：<strong>%s</strong></div>
              <div>该验证码将在 5 分钟后过期。</div>
            </div>
        """.formatted(code);

        emailUtil.sendHtmlEmail(email, subject, htmlContent);

        return BaseResponse.success("验证码已发送");
    }

    //注册
    @PostMapping("/register")
    public BaseResponse<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return BaseResponse.error(400,"两次输入的密码不一致");
        }
        return template.execute(status -> {
            if (userService.existsByUsername(request.getUsername())) {
                return BaseResponse.error(400, "用户名已存在");
            }
            if (userService.existsByEmail(request.getEmail())) {
                return BaseResponse.error(400, "邮箱已被注册");
            }
            VerificationCode code = userService.getVerificationCodeByEmail(request.getEmail());
            if (!code.getCode().equals(request.getVerificationCode())) {
                return BaseResponse.error(400, "验证码错误");
            }
            if (LocalDateTime.now().isAfter(code.getExpireTime())) {
                return BaseResponse.error(400, "验证码已过期");
            }
            if (code.getIsUsed() == 1) {
                return BaseResponse.error(400, "验证码已被使用");
            }
            // 5. 保存用户
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword()); // 建议在 User 中自动加密处理密码
            user.setUserType(0); // 默认为普通用户
            user.setCreateTime(LocalDateTime.now());
            user.setUsername("牛马人_" + GetRandomNumber.generateVerificationCode(6));
            userService.saveUser(user);
            // 6. 将验证码标记为已使用
            userService.markVerificationCodeAsUsed(request.getEmail());

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            String accessToken = JwtUtil.generateAccessToken(auth.getName());
            String refreshToken = JwtUtil.generateRefreshToken(auth.getName());

            return BaseResponse.success(new TokenResponse(accessToken, refreshToken), "注册成功");
        });
    }

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
