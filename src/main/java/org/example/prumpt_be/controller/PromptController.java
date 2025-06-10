//package org.example.prumpt_be.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.example.prumpt_be.dto.PromptDetailDTO;
//import org.example.prumpt_be.dto.request.PromptCreateRequestDto;
//import org.example.prumpt_be.dto.request.PromptUpdateRequestDto;
//import org.example.prumpt_be.service.PromptService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/prompts")
//public class PromptController {
//
//    private final PromptService promptService;
//
//    //프롬프트 상세 조회
//    @GetMapping("/{id}")
//    public ResponseEntity<PromptDetailDTO> getPromptDetail(@PathVariable Long id) {
//        PromptDetailDTO dto = promptService.getPromptDetail(id);
//        return ResponseEntity.ok(dto);
//    }
//
//    //프롬프트 전체 조회
//    @GetMapping
//    public ResponseEntity<List<PromptDetailDTO>> getAllPrompts() {
//        List<PromptDetailDTO> prompts = promptService.getAllPrompts();
//        return ResponseEntity.ok(prompts);
//    }
//
//
//    //프롬프트 등록
//    @PostMapping
//    public ResponseEntity<Long> createPrompt(@RequestBody PromptCreateRequestDto dto) {     //todo: 유저ID 입력받아서 본인확인 메커니즘 필요
//        Long id = promptService.savePrompt(dto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(id);
//    }
//
//    /**
//     * 프롬프트 수정
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<Void> updatePrompt(
//            @PathVariable Long id,     //todo: 유저ID 입력받아서 본인확인 메커니즘 필요
//            @RequestBody PromptUpdateRequestDto dto) {
//
//        promptService.updatePrompt(id, dto);
//        return ResponseEntity.ok().build();
//    }
//
//    //프롬프트 삭제
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deletePrompt(@PathVariable Long id) { //todo: 유저ID 입력받아서 검증하는 메커니즘 필요
//        promptService.deletePrompt(id);
//        return ResponseEntity.noContent().build(); // 204 No Content
//    }
//
//
//}
