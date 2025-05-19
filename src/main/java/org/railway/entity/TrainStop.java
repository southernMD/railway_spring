package org.railway.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.railway.entity.StationView;
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

    @Column(name = "train_id", nullable = false)
    private Long trainId;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private StationView station;

    @Column(nullable = false)
    private Integer sequence;

    @Column
    private LocalTime arrivalTime;

    @Column(columnDefinition = "int default 0")
    private Integer stopDuration;
}