package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {
    Optional<Users> findByAuth0Id(String auth0Id);
    Optional<Users> findByProfileName(String profileName); // 프로필 이름으로 찾는 경우 (필요시)
    Optional<Users> findByUserId(Integer userId); // 사용자 ID로 찾는 경우 (필요시)
}