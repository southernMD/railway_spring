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

    @Column(nullable = false)
    private Integer noSeatTickets = 0;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal businessPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal firstClassPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal secondClassPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal noSeatPrice;

}