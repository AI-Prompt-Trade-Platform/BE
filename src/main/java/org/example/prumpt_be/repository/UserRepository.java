package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
