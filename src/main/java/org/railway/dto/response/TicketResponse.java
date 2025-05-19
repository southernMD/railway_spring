package org.railway.dto.response;

import lombok.Data;
import org.railway.entity.Train;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
public class TicketResponse {
    private Long id;
    private String ticketNo;
    private Long orderId;
    private Long passengerId;
    private Train train;
    private LocalDate date;
    private Long departureStationId;
    private Long arrivalStationId;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private Integer seatType;
    private Long carriageId;
    private Long seatId;
    private BigDecimal price;
    private Integer status;
    private BigDecimal refundAmount;
    private LocalDateTime refundTime;
}
