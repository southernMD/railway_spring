package org.railway.dto.response;

import lombok.Data;
import org.railway.entity.Ticket;

import java.math.BigDecimal;

/**
 * 改签响应DTO，包含改签的详细信息
 */
@Data
public class ChangeRecordResponse {
    private Long id;
    private Ticket ticket;
    private Ticket newTicket;
    private Long orderId;
    private BigDecimal changeFee;
    private BigDecimal priceDifference;
    private Integer status;
    private String reason;
}
