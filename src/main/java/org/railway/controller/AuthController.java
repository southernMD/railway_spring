package org.railway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.railway.utils.JwtUserInfo;
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

/**
 * 认证控制器，提供用户注册、登录、验证码发送、Token刷新等功能。
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "认证模块", description = "提供用户认证相关的API接口")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final Email emailUtil;
    private final TransactionTemplate template;

    /**
     * 发送验证码
     * @param email 用户邮箱
     * @return 返回验证码发送结果
     */
    @Operation(
            summary = "发送验证码",
            description = "向指定邮箱发送验证码，验证码有效期为5分钟",
            responses = {
                    @ApiResponse(responseCode = "200", description = "验证码发送成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效")
            }
    )
    @RequestMapping(value = "/send-code", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResponse<String> sendVerificationCode(
            @Parameter(description = "用户邮箱", required = true)
            @RequestParam String email) {
        final String code = GetRandomNumber.generateVerificationCode(6);
        final LocalDateTime expireTime = LocalDateTime.now().plusMinutes(5);

        template.execute(status -> {
            userService.updateCodeAndExpireTime(email, code, expireTime,0);
            return null;
        });

        String subject = "注册验证码";
        String htmlContent = """
            <div style='display: flex; flex-direction: column; color:#222;'>
              <div>你好：</div>
              <div>你的验证码是：<strong>%s</strong></div>
              <div>该验证码将在 5 分钟后过期。</div>
            </div>
        """.formatted(code);

        emailUtil.sendHtmlEmail(email, subject, htmlContent);

        return BaseResponse.success("验证码已发送");
    }

    /**
     * 用户注册
     * @param request 注册请求DTO，包含用户名、邮箱、密码等信息
     * @return 返回注册结果及Token信息
     */
    @Operation(
            summary = "用户注册",
            description = "用户通过邮箱、用户名和验证码进行注册",
            responses = {
                    @ApiResponse(responseCode = "200", description = "注册成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效或验证码错误")
            }
    )
    @PostMapping("/register")
    public BaseResponse<TokenResponse> register(
            @Parameter(description = "注册请求信息", required = true)
            @Valid @RequestBody RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return BaseResponse.error(400, "两次输入的密码不一致");
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
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setUserType(0);
            user.setStatus(1);
            user.setCreateTime(LocalDateTime.now());
            user = userService.saveUser(user);
            userService.markVerificationCodeAsUsed(request.getEmail());

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            String accessToken = JwtUtil.generateAccessToken(auth.getName(),user.getId());
            String refreshToken = JwtUtil.generateRefreshToken(auth.getName(),user.getId());

            return BaseResponse.success(new TokenResponse(
                    accessToken,
                    refreshToken,
                    false,
                    user.getId(),
                    user.getUsername(),
                    user.getEmail()
            ), "注册成功");
        });
    }

    /**
     * 使用用户名登录
     * @param request 用户名登录请求DTO，包含用户名和密码
     * @return 返回登录结果及Token信息
     */
    @Operation(
            summary = "使用用户名登录",
            description = "用户通过用户名和密码进行登录",
            responses = {
                    @ApiResponse(responseCode = "200", description = "登录成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "401", description = "用户名或密码错误")
            }
    )
    @PostMapping("/login/username")
    public BaseResponse<TokenResponse> loginByUsername(
            @Parameter(description = "用户名登录请求信息", required = true)
            @Valid @RequestBody UsernameLoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = userService.findByUsername(request.getUsername());
        if(user.getStatus() == 0) {
            return BaseResponse.error(401, "用户已被封禁");
        }
        String accessToken = JwtUtil.generateAccessToken(auth.getName(),user.getId());
        String refreshToken = JwtUtil.generateRefreshToken(auth.getName(),user.getId());

        return BaseResponse.success(new TokenResponse(accessToken,
                refreshToken,
                user.getUserType() == 1,
                user.getId(),
                user.getUsername(),
                user.getEmail()
        ));
    }

    /**
     * 使用邮箱登录
     * @param request 邮箱登录请求DTO，包含邮箱和密码
     * @return 返回登录结果及Token信息
     */
    @Operation(
            summary = "使用邮箱登录",
            description = "用户通过邮箱和密码进行登录",
            responses = {
                    @ApiResponse(responseCode = "200", description = "登录成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "401", description = "邮箱或密码错误")
            }
    )
    @PostMapping("/login/email")
    public BaseResponse<TokenResponse> loginByEmail(
            @Parameter(description = "邮箱登录请求信息", required = true)
            @Valid @RequestBody EmailLoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = userService.findByEmail(request.getEmail());
        if(user.getStatus() == 0) {
            return BaseResponse.error(401, "用户已被封禁");
        }
        String accessToken = JwtUtil.generateAccessToken(auth.getName(),user.getId());
        String refreshToken = JwtUtil.generateRefreshToken(auth.getName(),user.getId());
        return BaseResponse.success(new TokenResponse(
                accessToken,
                refreshToken,
                user.getUserType() == 1,
                user.getId(),
                user.getUsername(),
                user.getEmail()
        ));
    }

    /**
     * 刷新Token
     * @param request HTTP请求对象，用于获取刷新Token
     * @param response HTTP响应对象
     * @return 返回新的AccessToken
     * @throws IOException 如果刷新Token无效，则抛出异常
     */
    @Operation(
            summary = "刷新Token",
            description = "使用刷新Token获取新的AccessToken",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token刷新成功"),
                    @ApiResponse(responseCode = "401", description = "刷新Token无效")
            }
    )
    @PostMapping("/refresh")
    public BaseResponse<AccessTokenResponse> refreshToken(
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = request.getHeader("Authorization");
        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
            if (JwtUtil.validateToken(refreshToken)) {
                JwtUserInfo userInfo = JwtUtil.extractUserInfo(refreshToken);
                String accessToken = JwtUtil.generateAccessToken(userInfo.getUsername(),userInfo.getUserId());
                return BaseResponse.success(new AccessTokenResponse(accessToken));
            }
        }
        return BaseResponse.error(401, "刷新Token无效");
    }
}
