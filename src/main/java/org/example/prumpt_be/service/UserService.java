package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.entity.Users;
import org.example.prumpt_be.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void addPointToUser(Long userId, int amount) {
        Users user = userRepository.findById(String.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        user.setPoint(user.getPoint() + amount);
        userRepository.save(user);
    }

}
