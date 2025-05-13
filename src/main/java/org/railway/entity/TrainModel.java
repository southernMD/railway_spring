package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "train_models")
@Data
@EqualsAndHashCode(callSuper = true)
public class TrainModel extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String modelName;

    @Column(nullable = false, length = 20, unique = true)
    private String modelCode;

    @Column(nullable = false)
    private Integer status = 1;

    @Column(nullable = false)
    private Integer maxCapacity;

    @Column(nullable = false)
    private Integer totalCarriages;

    @Column(nullable = false)
    private Integer businessCarriages = 0;

    @Column(nullable = false)
    private Integer firstClassCarriages = 0;

    @Column(nullable = false)
    private Integer secondClassCarriages = 0;

    @Column(nullable = false)
    private Integer businessSeatsPerCarriage = 0;

    @Column(nullable = false)
    private Integer firstClassSeatsPerCarriage = 0;

    @Column(nullable = false)
    private Integer secondClassSeatsPerCarriage = 0;

    @Column(columnDefinition = "TEXT")
    private String description;

}