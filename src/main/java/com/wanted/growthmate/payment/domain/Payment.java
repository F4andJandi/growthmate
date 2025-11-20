package com.wanted.growthmate.payment.domain;

import com.wanted.growthmate.payment.domain.enums.PaymentMethod;
import com.wanted.growthmate.payment.domain.enums.PaymentType;
import com.wanted.growthmate.payment.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "payments")
@Comment("결제 - 포인트 결제 및 환불 내역")
@DiscriminatorValue("PAYMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // protected 기본 생성자
@AllArgsConstructor(access = AccessLevel.PRIVATE)   // private 모든 필드 생성자
@Builder
public class Payment extends PointTransaction {

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    @Comment("결제 수단")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    @Comment("결제 유형(결제, 환불)")
    private PaymentType paymentType;

    @Column(name = "amount", nullable = false)
    @Comment("실제 결제 금액 (원)")
    private Integer amount;

    // NOTE: [1차 구현] amount:point_amount = 1:1
    @Column(name = "point_amount", nullable = false)
    @Comment("충전될 포인트 금액 (P)")
    private Integer pointAmount;

    // TODO: 향후 결제 대행사, 결제 상태, 결제 완료/취소 일시 등의 필드 추가 가능

    // NOTE: 정적 팩토리 메서드 (객체 직접 생성 방지) ---------------------
    /*
     * [결제] 타입의 Payment 생성
     */
    public static Payment createPurchasePayment(
            Point point,
            PaymentMethod paymentMethod,
            Integer amount
    ) {
        Payment payment = Payment.builder()
                .paymentMethod(paymentMethod)
                .paymentType(PaymentType.PURCHASE)
                .amount(amount)
                .pointAmount(amount)  // NOTE: [결제 금액:포인트] = 1:1 -> 비율 정책이 변경되면 여기서 수정
                .build();

        // ---- 상속받은 메서드 ----
        // "이 결제는 특정 사용자의 포인트 원장에 속한다”는 관계를 연결해주는 코드
        payment.setPoint(point);
        // PointTransaction에 “공통 거래정보”를 기록하는 메서드
        payment.recordTransaction(
                TransactionType.PAYMENT_EARN,
                amount,
                "포인트 충전(" + PaymentMethod.CARD.getDescription() + ")"
        );
        // ---------------------

        return payment;
    }

    // TODO: [환불] 타입의 Payment 생성

    // ----------------------------------------------------------
}
