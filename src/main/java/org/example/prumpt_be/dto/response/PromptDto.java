package org.example.prumpt_be.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PromptDto {
    private int promptID;
    private String promptName;
    private String promptContent;
    private Integer price;
    private List<ReviewDto> reviews = new ArrayList<>();

    public PromptDto(int promptID, String promptName, String promptContent, Integer price) {
        this.promptID = promptID;
        this.promptName = promptName;
        this.promptContent = promptContent;
        this.price = price;
    }
}


