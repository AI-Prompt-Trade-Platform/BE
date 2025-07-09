package org.example.prumpt_be.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationDTO {
    private String modelName;
    private String typeName;
}
