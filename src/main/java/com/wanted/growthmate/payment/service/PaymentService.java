package com.wanted.growthmate.payment.service;

import com.wanted.growthmate.payment.dto.PointChargeRequest;

public interface PaymentService {
    // 결제하여 포인트 충전 시 포인트 적립
    void chargePoints(Long userId, PointChargeRequest dto);
}
