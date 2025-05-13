package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_logs")
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemLog extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 50)
    private String module;

    @Column(nullable = false, length = 50)
    private String operation;

    @Column(length = 100)
    private String method;

    @Column(columnDefinition = "TEXT")
    private String params;

    @Column(length = 50)
    private String ip;

    private Integer status;

    @Column(columnDefinition = "TEXT")
    private String errorMsg;

    @Column(nullable = false, updatable = false)
    private LocalDateTime operationTime = LocalDateTime.now();
}