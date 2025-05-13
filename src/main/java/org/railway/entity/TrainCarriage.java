package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "train_carriages")
@Data
@EqualsAndHashCode(callSuper = true)
public class TrainCarriage extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private TrainModel model;

    @Column(nullable = false, length = 10)
    private String carriageNumber;

    @Column(nullable = false)
    private Integer carriageType;

    @Column(nullable = false)
    private Integer seatCount;

}