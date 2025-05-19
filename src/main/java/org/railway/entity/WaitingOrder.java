package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "waiting_orders")
@Data
@EqualsAndHashCode(callSuper = true)
public class WaitingOrder extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    @Column(nullable = false)
    private Integer passengerCount = 1;

    @Column(nullable = false)
    private Integer status = 0;

    @Column(nullable = false)
    private LocalDateTime expireTime;

}