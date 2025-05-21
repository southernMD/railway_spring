package org.railway.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 候补订单响应DTO，包含候补订单的详细信息
 */
@Data
public class WaitingOrderResponse {
    private Long id;
    private Long userId;
    private Long trainId;
    private LocalDate date;
    private Long departureStationId;
    private Long arrivalStationId;
    private Integer seatType;
    private Integer passengerCount;
    private Integer status;
    private LocalDateTime expireTime;
}
