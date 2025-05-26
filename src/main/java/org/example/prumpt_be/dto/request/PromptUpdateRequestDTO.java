// dto/request/PromptUpdateRequestDTO.java
package org.example.prumpt_be.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class PromptUpdateRequestDTO {
    private String title;
    private String description;
    private String content;
    private int price;
    private Long categoryId;
    private List<String> tags;
}
