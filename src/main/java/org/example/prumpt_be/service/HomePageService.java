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

    public List<Prompt> getPopularPrompts() {
        return promptRepository.findTop10ByOrderByPriceDesc();
    }

    public List<Prompt> getLatestPrompts() {
        return promptRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public List<Prompt> getPromptsByModel(String model) {
        return promptRepository.findByModel(model);
    }

    public List<Prompt> getPromptsByType(String type) {
        return promptRepository.findByType(type);
    }

    public List<User> getPopularCreators() {
        return userRepository.findTop5ByOrderByPointDesc();
    }

    public List<Prompt> searchPrompts(String keyword) {
        return promptRepository.findByPromptNameContaining(keyword);
    }
}
