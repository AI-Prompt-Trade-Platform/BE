// /Users/madecafe/workSpace/Prumpt_2nd_Prj/Prumpt_BE/src/main/java/org/example/prumpt_be/dto/request/ChatCompletionRequestDto.java
package org.example.prumpt_be.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChatCompletionRequestDto {
    private String model;
    private List<Message> messages;

    /**
     * OpenAI에 보낼 메시지를 나타냅니다.
     * 텍스트만 보낼 수도 있고, 텍스트와 이미지를 함께 보낼 수도 있습니다.
     */
    @Data
    @NoArgsConstructor
    public static class Message {
        private String role;
        // content는 String(텍스트만 있을 경우) 또는 List<MessageContent>(이미지 포함 시)가 될 수 있습니다.
        private Object content;

        // 텍스트 전용 메시지를 위한 생성자
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        // 이미지와 텍스트를 함께 보내는 복합 메시지를 위한 생성자
        public Message(String role, List<MessageContent> content) {
            this.role = role;
            this.content = content;
        }
    }

    /**
     * 메시지의 각 부분을 나타냅니다. (e.g., 텍스트 부분, 이미지 부분)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL) // JSON으로 변환 시 null 필드는 제외
    public static class MessageContent {
        private String type;
        private String text;
        private ImageUrl image_url;

        // 텍스트 콘텐츠를 쉽게 생성하기 위한 정적 팩토리 메서드
        public static MessageContent ofText(String text) {
            return new MessageContent("text", text, null);
        }

        // 이미지 URL 콘텐츠를 쉽게 생성하기 위한 정적 팩토리 메서드
        public static MessageContent ofImageUrl(ImageUrl imageUrl) {
            return new MessageContent("image_url", null, imageUrl);
        }
    }

    /**
     * 이미지의 URL을 담는 객체입니다.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageUrl {
        private String url;
    }
}