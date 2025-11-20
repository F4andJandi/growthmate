package com.wanted.growthmate.payment.service;

import com.wanted.growthmate.payment.domain.Payment;
import com.wanted.growthmate.payment.domain.Point;
import com.wanted.growthmate.payment.dto.PointChargeRequest;
import com.wanted.growthmate.payment.exception.PointNotFoundException;
import com.wanted.growthmate.payment.repository.PaymentRepository;
import com.wanted.growthmate.payment.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PointRepository pointRepository;

    /*
     * 결제하여 포인트 충전 시 포인트 적립
     */
    @Transactional
    public void chargePoints(Long userId, PointChargeRequest dto) {
        // 1. 포인트 원장 조회
        Point point = pointRepository.findByUserId(userId)
                .orElseThrow(() -> new PointNotFoundException(userId));

        // 2. 결제(Payment) 엔티티 생성
        Payment payment = Payment.createPurchasePayment(point, dto.getPaymentMethod(), dto.getAmount());

        // 3. 저장 (JOINED 전략으로 point_transactions + payments에 각각 insert)
        paymentRepository.save(payment);

        // 4. 포인트 잔액 업데이트
        point.addBalance(dto.getAmount());
    }
}
