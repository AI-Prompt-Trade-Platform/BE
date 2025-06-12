package org.example.prumpt_be.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointPurchaseRequest {
    private String paymentKey;
    private String orderId;
    private int amount;  // 충전할 포인트 (결제 금액과 동일)
}
