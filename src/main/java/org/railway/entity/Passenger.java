package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "passengers")
@Data
@EqualsAndHashCode(callSuper = true)
public class Passenger extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String realName;

    @Column(nullable = false, length = 18)
    private String idCard;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false)
    private Integer passengerType = 1;

    @Column(nullable = false)
    private Integer isDefault = 0;

    @Column(nullable = false)
    private Integer status = 1;

}