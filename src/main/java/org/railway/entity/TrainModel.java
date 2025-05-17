package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

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
    private Integer status;

    @Column(nullable = false)
    private Integer maxCapacity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "model_id") // 指定外键字段名
    private List<TrainCarriage> carriages;

}