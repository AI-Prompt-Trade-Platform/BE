//package org.example.prumpt_be.controller;
//
//import com.nimbusds.openid.connect.sdk.Prompt;
//import org.example.prumpt_be.repository.PurchasesRepository;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/prompts")
//public class PromptController {
//
//  private final PurchasesRepository purchasesRepository;
//
//  public PromptController(PurchasesRepository purchasesRepository) {
//    this.purchasesRepository = PromptController.this.purchasesRepository;
//  }
//
//  // 인증된 사용자가 볼 수 있는 프롬프트 목록 조회 예시
//  @GetMapping
//  public List<PromptResponseDto> listPrompts(
//          @AuthenticationPrincipal Jwt jwt
//  ) {
//    // 1) JWT에서 sub(사용자 ID) 뽑아오기
//    String userId = jwt.getSubject();
//
//    // 2) 서비스 호출: 해당 사용자가 볼 수 있는 프롬프트 목록 조회
//    List<Prompt> prompts = purchasesRepository.listByUser(userId);
//
//    // 3) 엔티티 → DTO 변환
//    return prompts.stream()
//            .map(PromptResponseDto::fromEntity)
//            .collect(Collectors.toList());
//  }
//}
//
