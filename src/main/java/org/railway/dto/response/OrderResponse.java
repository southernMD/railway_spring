package org.railway.dto.response;

import lombok.Data;
import org.railway.entity.Ticket;
import org.railway.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String orderNo;
    private User user;
    private BigDecimal totalAmount;
    private List<Ticket> tickets;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
