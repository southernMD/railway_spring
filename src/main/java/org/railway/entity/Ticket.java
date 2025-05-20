package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;

@Entity
@Table(name = "tickets")
@Data
@EqualsAndHashCode(callSuper = true)
public class Ticket extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32, unique = true)
    private String ticketNo;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "departure_station_id", nullable = false)
    private StationView departureStation;

    @ManyToOne
    @JoinColumn(name = "arrival_station_id", nullable = false)
    private StationView arrivalStation;

    @Column(nullable = false)
    private Integer seatType;

    @ManyToOne
    @JoinColumn(name = "carriage_id")
    private TrainCarriage carriage;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer status;

    @Column(precision = 10, scale = 2)
    private BigDecimal refundAmount;

    private LocalDateTime refundTime;

}