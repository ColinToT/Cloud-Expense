package com.cloudexpense.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * ClassName: User
 * Package: com.cloudexpense.user.entity
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/22 21:02
 * @Version: v1.0
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private Long managerId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
