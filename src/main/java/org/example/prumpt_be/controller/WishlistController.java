package org.example.prumpt_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.response.WishlistPromptResponse;
import org.example.prumpt_be.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping("/{userId}/wishlist")
    public List<WishlistPromptResponse> getUserWishlist(@PathVariable Long userId) {
        return wishlistService.getUserWishlist(userId);
    }

    @PostMapping("/{userId}/wishlist/{promptId}")
    public void addToWishlist(@PathVariable Long userId, @PathVariable Long promptId) {
        wishlistService.addToWishlist(userId, promptId);
    }

    @DeleteMapping("/{userId}/wishlist/{promptId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long userId, @PathVariable Long promptId) {
        wishlistService.removeFromWishlist(userId, promptId);
        return ResponseEntity.noContent().build();
    }

}