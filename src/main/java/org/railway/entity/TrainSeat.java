package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "train_seats")
@Data
@EqualsAndHashCode(callSuper = true)
public class TrainSeat extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Integer businessSeats = 0;

    @Column(nullable = false)
    private Integer firstClassSeats = 0;

    @Column(nullable = false)
    private Integer secondClassSeats = 0;

    @Column(nullable = false)
    private Integer noSeatTickets = 0;

    @Column(nullable = false)
    private Integer availableBusinessSeats = 0;

    @Column(nullable = false)
    private Integer availableFirstClassSeats = 0;

    @Column(nullable = false)
    private Integer availableSecondClassSeats = 0;

    @Column(nullable = false)
    private Integer availableNoSeatTickets = 0;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal businessPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal firstClassPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal secondClassPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal noSeatPrice;

}