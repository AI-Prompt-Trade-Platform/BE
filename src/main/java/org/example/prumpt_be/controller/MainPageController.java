package org.example.prumpt_be.controller;


import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.service.MainPageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor  //final 필드에 대한 생성자주입
@RequestMapping("/")
public class MainPageController {
    private final MainPageService mainPageService;

    //Swagger UI : API 자동 문서화 프레임워. 컨트롤러의 각 메소드 상단에 아래 내용을 붙여서 사용
//    @Operation(summary = "단일 프롬프트 조회", description = "ID로 프롬프트 한 건을 조회합니다.") -> 해당 API 설명
//    @ApiResponse(responseCode = "200", description = "성공",  -> 응답코드 설명
//            content = @Content(schema = @Schema(implementation = PromptDto.class)) -> 반환하는 JSON객체의 타 나타냄
//    )

}
