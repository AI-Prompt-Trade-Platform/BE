package org.example.prumpt_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TossConfirmRequestDto {
    private String paymentKey;
    private String orderId;
    private int amount;
}
