package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.entity.Users;
import org.example.prumpt_be.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    @Transactional
    public void addPointToUser(String userAuth0Id, int amount) {
        Users user = userRepository.findByAuth0Id(userAuth0Id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        // 기존 포인트가 null일 경우를 대비하여 안전하게 처리
        int currentPoint = user.getPoint() != null ? user.getPoint() : 0;
        user.setPoint(currentPoint + amount);

        userRepository.save(user);
    }

}
