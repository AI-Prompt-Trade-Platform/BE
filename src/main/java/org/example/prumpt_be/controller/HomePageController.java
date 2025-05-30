// HomePageController.java
package org.example.prumpt_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.User;
import org.example.prumpt_be.service.HomePageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prompts")
public class HomePageController {

    private final HomePageService homePageService;

    @GetMapping("/popular/prompts")
    public List<Prompt> getPopularPrompts() {
        return homePageService.getPopularPrompts();
    }

    @GetMapping("/latest")
    public List<Prompt> getLatestPrompts() {
        return homePageService.getLatestPrompts();
    }

    @GetMapping("/category/model/{model}")
    public List<Prompt> getPromptsByModel(@PathVariable String model) {
        return homePageService.getPromptsByModel(model);
    }

    @GetMapping("/category/type/{type}")
    public List<Prompt> getPromptsByType(@PathVariable String type) {
        return homePageService.getPromptsByType(type);
    }

    @GetMapping("/popular/creators")
    public List<User> getPopularCreators() {
        return homePageService.getPopularCreators();
    }

    @GetMapping("/search")
    public List<Prompt> searchPrompts(@RequestParam("q") String keyword) {
        return homePageService.searchPrompts(keyword);
    }
}
