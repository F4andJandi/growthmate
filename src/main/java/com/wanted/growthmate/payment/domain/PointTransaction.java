package com.wanted.growthmate.payment.domain;

import com.wanted.growthmate.common.entity.BaseTimeEntity;
import com.wanted.growthmate.payment.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="dtype")
@Table(name = "point_transactions")
@Comment("포인트 거래 내역 - 모든 포인트 변동 이력 추적 (감사 추적용)")
@Getter
public abstract class PointTransaction extends BaseTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id", nullable = false)
    @Comment("포인트")
    private Point point; // 어느 원장의 거래인지

    // FIXME: Enrollment랑 직접 연결하지 않고, EnrollmentTransaction 통해서 연결
    // -> 다음 PR에서 다시 고려하겠습니다.
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "enrollment_id", nullable = true)
    //@Comment("수강 ID")
    //private Enrollment enrollment;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    @Comment("거래 유형")
    private TransactionType transactionType;

    @Column(name = "amount", nullable = false)
    @Comment("거래 포인트 (양수: 적립, 음수: 차감)")
    private Integer amount; // 적립(+) / 차감(-) 금액

    @Column(name = "balance_after", nullable = false)
    @Comment("거래 후 잔액")
    private Integer balanceAfter;

    @Column(name = "description", nullable = false)
    @Comment("거래 설명")
    private String description;

    protected PointTransaction() {}

    protected void setPoint(Point point) {
        this.point = point;
    }

    /*
     * 공통 거래 초기화 로직
     */
    protected void recordTransaction(TransactionType transactionType, int amount, String description) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = point.getBalance() + amount;
        this.description = description;
    }
}
