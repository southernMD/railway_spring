package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.railway.entity.Station;
import org.railway.entity.Train;

import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "train_stops")
@Data
@EqualsAndHashCode(callSuper = true)
public class TrainStop extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(nullable = false)
    private Integer sequence;

    @Column
    private LocalTime arrivalTime;

    @Column
    private LocalTime departureTime;

    @Column(columnDefinition = "int default 0")
    private Integer stopDuration = 0;
}