package org.example.prumpt_be.repository;

import org.example.prumpt_be.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    //프로필명으로 사용자 조회
    @Query
    List<Users> findByProfileName(String profile_name);
}

