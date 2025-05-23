package org.railway.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 座位锁定创建请求参数
 */
@Data
public class SeatLockRequest {
    @NotNull(message = "座位ID不能为空")
    private Integer seatId;

    @NotNull(message = "锁定开始时间不能为空")
    private LocalDateTime lockStart;

    @NotNull(message = "锁定过期时间不能为空")
    private LocalDateTime expireTime;

    private String reason;

    @NotNull(message = "锁定类型不能为空")
    private Integer type;
}
