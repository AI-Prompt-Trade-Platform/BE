package org.example.prumpt_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.response.UserMypageResponse;
import org.example.prumpt_be.service.UserProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{userId}/profile")
    public UserMypageResponse getUserProfile(@PathVariable Long userId) {
        return userProfileService.getUserProfile(userId);
    }

}