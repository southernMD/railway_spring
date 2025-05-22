package org.railway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.railway.dto.request.UserRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.UserResponse;
import org.railway.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "用户管理", description = "提供用户的CRUD操作")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @Operation(summary = "创建管理员用户", description = "根据请求数据创建新用户")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse<UserResponse> createUser(@RequestBody UserRequest userRequestDTO) {
        return BaseResponse.success(userService.createAdmin(userRequestDTO));
    }

    @Operation(summary = "获取所有用户", description = "获取所有用户信息")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse<List<UserResponse>> getAllUsers() {
        return BaseResponse.success(userService.getAllUsers());
    }

    @Operation(summary = "封禁账号", description = "根据用户ID封禁账号")
    @PutMapping("/{userId}/ban")
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse<Object> banUser(@PathVariable Long userId) {
        userService.banUser(userId);
        return BaseResponse.success(null, "封禁成功");
    }

    @Operation(summary = "解封账号", description = "根据用户ID解禁账号")
    @PutMapping("/{userId}/unBan")
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse<Object> unBanUser(@PathVariable Long userId) {
        userService.unBanUser(userId);
        return BaseResponse.success(null, "解禁成功");
    }

}
