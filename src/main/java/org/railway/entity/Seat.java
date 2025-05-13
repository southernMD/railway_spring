package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "seats")
@Data
@EqualsAndHashCode(callSuper = true)
public class Seat extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private TrainModel model;

    @ManyToOne
    @JoinColumn(name = "carriage_id", nullable = false)
    private TrainCarriage carriage;

    @Column(nullable = false, length = 10)
    private String seatNumber;

    @Column(nullable = false)
    private Integer seatType;

    @Column(nullable = false)
    private Integer rowNumber;

    @Column(nullable = false, length = 5)
    private String columnNumber;

    @Column(nullable = false)
    private Integer status = 1;

}