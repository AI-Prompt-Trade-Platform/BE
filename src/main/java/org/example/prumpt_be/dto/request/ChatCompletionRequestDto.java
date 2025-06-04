package org.example.prumpt_be.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class ChatCompletionRequestDto {
    private String model;
    private List<Message> messages;

    @Data
    public static class Message {
        private String role;
        private String content;

        public Message(String user, String prompt) {
            this.role = user;
            this.content = prompt;
        }
    }
}
