package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAuth0Id(String auth0Id);
    Optional<User> findByProfileName(String profileName); // 프로필 이름으로 찾는 경우 (필요시)
}