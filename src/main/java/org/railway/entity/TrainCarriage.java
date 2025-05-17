package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "train_carriages")
@Data
@EqualsAndHashCode(callSuper = true)
public class TrainCarriage extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id", nullable = false)
    private Integer modelId;

    @Column(nullable = false, length = 10)
    private Integer carriageNumber;

    @Column(nullable = false)
    private Integer carriageType;

    @Column(nullable = false)
    private Integer seatCount;

    // 车厢与座位是一对多关系
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "carriage_id") // 指定外键字段名
    private List<Seat> seats;
}