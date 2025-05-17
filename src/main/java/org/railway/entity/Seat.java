package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "seat_id", insertable = false, updatable = false)
    private SeatLock lockInfo;
}