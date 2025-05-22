package org.railway.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 100, unique = true)
    private String email;

    @Column(nullable = false)
    private Integer userType;

    @Column(nullable = false)
    private Integer status;

}