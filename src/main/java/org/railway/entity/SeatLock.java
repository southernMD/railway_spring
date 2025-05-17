package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "seat_locks")
@Data
@EqualsAndHashCode(callSuper = true)
public class SeatLock extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_id", nullable = false)
    private Long seatId;

    @Column(nullable = false)
    private LocalDateTime expireTime;

    @Column(nullable = false)
    private LocalDateTime lockStart;

    private String reason;

    private Integer finish = 0;
}