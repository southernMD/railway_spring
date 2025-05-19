package org.railway.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
public class TicketRequest {

    @NotNull(message = "票号不能为空")
    @Size(min = 1, max = 32, message = "票号长度必须在1到32之间")
    private String ticketNo;

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

    @NotNull(message = "出发时间不能为空")
    private LocalTime departureTime;

    @NotNull(message = "到达时间不能为空")
    private LocalTime arrivalTime;

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
    @Max(value = 3, message = "状态值必须小于等于2")
    private Integer status;

    private BigDecimal refundAmount;

    private LocalDateTime refundTime;
}
