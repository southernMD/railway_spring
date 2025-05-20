package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Table(name = "change_records")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChangeRecord extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket originalTicket;

    @ManyToOne
    @JoinColumn(name = "new_ticket_id")
    private Ticket newTicket;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal changeFee = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceDifference = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer status;

    @Column(length = 200)
    private String reason;
}