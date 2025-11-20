package com.wanted.growthmate.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PointTransactionSummary {
    private final LocalDateTime createdAt;
    private final String description;
    private final Integer amount;
    private final Integer balanceAfter;
}
