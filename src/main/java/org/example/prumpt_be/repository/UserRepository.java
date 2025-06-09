package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
}
