package org.railway.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 候补订单请求DTO，包含候补订单的详细信息
 */
@Data
public class WaitingOrderRequest {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "列车ID不能为空")
    private Long trainId;

    @NotNull(message = "日期不能为空")
    private LocalDate date;

    @NotNull(message = "出发站ID不能为空")
    private Long departureStationId;

    @NotNull(message = "到达站ID不能为空")
    private Long arrivalStationId;

    @NotNull(message = "座位类型不能为空")
    private Integer seatType;

    @NotNull(message = "乘客数量不能为空")
    private Integer passengerCount;

}
