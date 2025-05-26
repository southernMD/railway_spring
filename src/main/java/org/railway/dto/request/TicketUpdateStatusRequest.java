package org.railway.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketUpdateStatusRequest {
    @NotNull(message = "车票ID不能为空")
    private Long id;

    @NotNull(message = "状态不能为空")
    private Integer status;

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotNull(message = "用户ID不能为空")
    private Long userId;
}
