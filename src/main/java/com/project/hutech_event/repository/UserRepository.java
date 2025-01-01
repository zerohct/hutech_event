package com.project.hutech_event.repository;


import com.project.hutech_event.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    // Kiểm tra sự tồn tại của username (có thể là email hoặc studentCode)
    boolean existsByEmail(String email);
    boolean existsByStudentCode(String studentCode);

    // Lấy User theo email hoặc studentCode
    Optional<User> findByEmail(String email);
    Optional<User> findByStudentCode(String studentCode);
}
