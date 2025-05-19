package org.railway.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.railway.dto.Views;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "trains")
@Data
@EqualsAndHashCode(callSuper = true)
public class Train extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String trainNumber;

    @OneToOne
    @JoinColumn(name = "model_id", nullable = false)
    @JsonView(Views.Other.class)
    private TrainModel model;

    @OneToOne
    @JoinColumn(name = "start_station_id", nullable = false)
    private StationView startStation;

    @OneToOne
    @JoinColumn(name = "end_station_id", nullable = false)
    private StationView endStation;

    @Column(nullable =  false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime departureTime;

    @Column(nullable = false)
    private LocalTime arrivalTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "train_seats_id", referencedColumnName = "id")
    private TrainSeat trainSeatInfo;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "train_id") // 使用 @JoinColumn 维护关系
    @OrderBy("sequence ASC")
    private List<TrainStop> trainStops;

}