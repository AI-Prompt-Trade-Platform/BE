package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findTop5ByOrderByPointDesc();
}