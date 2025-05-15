package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_codes")
@Data
@EqualsAndHashCode(callSuper = true)
public class VerificationCode extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String email;

    @Column(nullable = false, length = 10)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expireTime;

    @Column(nullable = false)
    private Integer isUsed = 0;

}