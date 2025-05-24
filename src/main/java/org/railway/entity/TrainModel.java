package org.railway.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.railway.dto.Views;

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
    @JsonView(Views.Detailed.class)
    private Integer status;

    @Column(nullable = false)
    private Integer maxCapacity;

    @Column(columnDefinition = "TEXT")
    @JsonView(Views.Detailed.class)
    private String description;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "model_id") // 指定外键字段名
    @JsonView(Views.Detailed.class)
    private List<TrainCarriage> carriages;

}