package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "trains")
@Data
@EqualsAndHashCode(callSuper = true)
public class Train extends Base{
    @Id
    @Column(length = 20)
    private String id;

    @Column(nullable = false, length = 20)
    private String trainNumber;

    @Column(nullable = false, length = 10)
    private String trainType;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private TrainModel model;

    @ManyToOne
    @JoinColumn(name = "start_station_id", nullable = false)
    private Station startStation;

    @ManyToOne
    @JoinColumn(name = "end_station_id", nullable = false)
    private Station endStation;

    @Column(nullable = false)
    private LocalTime departureTime;

    @Column(nullable = false)
    private LocalTime arrivalTime;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private Integer status = 1;

}