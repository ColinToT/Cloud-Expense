package com.cloudexpense.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

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
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "role", nullable = false)
    private Role role;

    private Long managerId;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
