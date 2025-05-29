package org.example.prumpt_be.dto.response;

public class PromptPurchaseRequest {
    private Integer promptId;
    private Integer buyerId;

    public Integer getPromptId() {
        return promptId;
    }

    public void setPromptId(Integer promptId) {
        this.promptId = promptId;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }
}