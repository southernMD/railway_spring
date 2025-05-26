package org.railway.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.railway.entity.Ticket;

/**
 * 改签请求DTO，包含改签的基本信息
 */
@Data
public class ChangeRecordRequest {
    @NotNull(message = "原票ID不能为空")
    private Long ticketId;

    @NotNull(message = "新票不能为空")
    private TicketRequest newTicket;

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    private Long userId;
}
