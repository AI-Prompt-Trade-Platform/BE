package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.User;
import org.example.prumpt_be.repository.PromptRepository;
import org.example.prumpt_be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomePageService {

    private final PromptRepository promptRepository;
    private final UserRepository userRepository;

    // 인기 프롬프트 목록 반환
    public List<Prompt> getPopularPrompts() {
        return promptRepository.findTop10ByOrderByPriceDesc();
    }

    // 최신 프롬프트 목록 반환
    public List<Prompt> getLatestPrompts() {
        return promptRepository.findTop10ByOrderByCreatedAtDesc();
    }

    // 카테고리별 프롬프트 목록 반환 (모델 기준)
    public List<Prompt> getPromptsByModel(String model) {
        return promptRepository.findByModel(model);
    }

    // 카테고리별 프롬프트 목록 반환 (종류 기준)
    public List<Prompt> getPromptsByType(String type) {
        return promptRepository.findByType(type);
    }

    // 인기 크리에이터 목록 반환
    public List<User> getPopularCreators() {
        return userRepository.findTop5ByOrderByPointDesc();
    }

    // 프롬프트 검색
    public List<Prompt> searchPrompts(String keyword) {
        return promptRepository.findByPromptNameContaining(keyword);
    }
}
