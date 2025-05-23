package org.example.prumpt_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.PromptDetailDTO;
import org.example.prumpt_be.service.PromptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prompts")
public class PromptController {

    private final PromptService promptService;

    @GetMapping("/{id}")
    public ResponseEntity<PromptDetailDTO> getPromptDetail(@PathVariable Long id) {
        PromptDetailDTO dto = promptService.getPromptDetail(id);
        return ResponseEntity.ok(dto);
    }
}
