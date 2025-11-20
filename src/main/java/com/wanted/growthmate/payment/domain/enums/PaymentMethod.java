package com.wanted.growthmate.payment.domain.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CARD("카드결제");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }
}
