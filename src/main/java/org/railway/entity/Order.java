package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@EqualsAndHashCode(callSuper = true)
public class Order extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32, unique = true)
    private String orderNo;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String contactName;

    @Column(nullable = false, length = 20)
    private String contactPhone;

    @Column(nullable = false)
    private Integer passengerCount = 1;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private Integer status = 0;

    @Column(length = 20)
    private String paymentMethod;

    private LocalDateTime paymentTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Ticket> tickets;
}