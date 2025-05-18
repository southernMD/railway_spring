package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seats")
@Data
@EqualsAndHashCode(callSuper = true)
public class Seat extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "carriage_id", nullable = false)
    private Long carriageId;

    @Column(nullable = false, length = 10)
    private String seatNumber;

    @Column(nullable = false)
    private Integer status = 1;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private List<SeatLock> lockInfo = new ArrayList<>();
}