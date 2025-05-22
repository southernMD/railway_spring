package org.railway.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "用户响应DTO，用于返回用户信息")
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private Integer userType;

    private Integer status;

    private LocalDateTime createTime;
}
