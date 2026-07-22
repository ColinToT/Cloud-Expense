package com.cloudexpense.user.repository;

import com.cloudexpense.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ClassName: UserRepository
 * Package: com.cloudexpense.user.repository
 * Description:
 *
 * @Author: Colin
 * @Create: 2026/7/22 21:24
 * @Version: v1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
