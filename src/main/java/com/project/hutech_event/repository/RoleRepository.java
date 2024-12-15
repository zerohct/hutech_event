package com.project.hutech_event.repository;

import com.project.hutech_event.model.Role;
import com.project.hutech_event.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    Optional<Role> findByName(String username);
}
