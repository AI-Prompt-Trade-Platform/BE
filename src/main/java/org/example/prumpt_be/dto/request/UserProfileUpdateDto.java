package org.example.prumpt_be.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter // Controller에서 @RequestBody로 받기 위해 Setter 또는 생성자 필요
public class UserProfileUpdateDto {
    private String profileName;
    private String introduction;
    private MultipartFile profileImg; // URL을 직접 받거나, 파일 업로드 후 URL을 서비스에서 설정
    private MultipartFile bannerImg;  // 위와 동일
}