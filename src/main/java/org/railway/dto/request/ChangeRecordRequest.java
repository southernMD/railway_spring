package org.railway.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 改签请求DTO，包含改签的基本信息
 */
@Data
public class ChangeRecordRequest {
    @NotNull(message = "原票ID不能为空")
    private Long ticketId;

    @NotNull(message = "新票ID不能为空")
    private Long newTicketId;

    @NotNull(message = "订单ID不能为空")
    private Long orderId;
}
