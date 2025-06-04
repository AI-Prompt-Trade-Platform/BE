package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

    //프로필명으로 사용자 조회
    @Query
    List<Users> findByProfileName(String profile_name);

    // Auth0 ID(sub)로 사용자 조회 (Auth0ID => UserId 로 변경 용도)
    Optional<Users> findByAuth0Id(String auth0Id);
}

