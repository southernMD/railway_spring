package org.railway.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TicketRequest {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotNull(message = "乘客ID不能为空")
    private Long passengerId;

    @NotNull(message = "列车ID不能为空")
    private Long trainId;

    @NotNull(message = "日期不能为空")
    private LocalDate date;

    @NotNull(message = "出发站ID不能为空")
    private Long departureStationId;

    @NotNull(message = "到达站ID不能为空")
    private Long arrivalStationId;

    @NotNull(message = "座位类型不能为空")
    @Min(value = 1, message = "座位类型值必须大于等于0")
    @Max(value = 4, message = "座位类型值必须小于等于4")
    private Integer seatType;

    private Long carriageId;

    private Long seatId;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.0", inclusive = false, message = "价格必须大于0")
    private BigDecimal price;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值必须大于等于0")
    @Max(value = 5, message = "状态值必须小于等于5")
    private Integer status;
//
//    private BigDecimal refundAmount;
//
//    private LocalDateTime refundTime;
}
