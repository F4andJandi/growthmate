package com.wanted.growthmate.payment.domain;

import com.wanted.growthmate.enrollment.entity.Enrollment;
import com.wanted.growthmate.payment.domain.enums.PaymentMethod;
import com.wanted.growthmate.payment.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "enrollment_transactions")
@DiscriminatorValue("ENROLLMENT_TRANSACTION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Comment("수강 트랜잭션 - 수강 시 발생하는 포인트 이동 내역")
public class EnrollmentTransaction extends PointTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    @Comment("관련 수강 정보")
    private Enrollment enrollment;

    public Enrollment getEnrollment() {
        return enrollment;
    }

    // NOTE: 정적 팩토리 메서드 (객체 직접 생성 방지) ---------------------
    public static EnrollmentTransaction createForStudent(
            Enrollment enrollment,
            Point point,
            int amount
    ) {
        EnrollmentTransaction enrollmentTransaction = EnrollmentTransaction.builder()
                .enrollment(enrollment)
                .build();

        // "이 결제는 특정 사용자의 포인트 원장에 속한다”는 관계를 연결해주는 코드
        enrollmentTransaction.setPoint(point);
        // PointTransaction에 “공통 거래정보”를 기록하는 메서드
        enrollmentTransaction.recordTransaction(
                TransactionType.ENROLLMENT_STUDENT_USE,
                amount,
                "수강 신청(" + PaymentMethod.CARD.getDescription() + ")"
        );

        return enrollmentTransaction;
    }

    public static EnrollmentTransaction createForInstructor(
            Enrollment enrollment,
            Point point,
            int amount
    ) {
        EnrollmentTransaction enrollmentTransaction = EnrollmentTransaction.builder()
                .enrollment(enrollment)
                .build();

        // "이 결제는 특정 사용자의 포인트 원장에 속한다”는 관계를 연결해주는 코드
        enrollmentTransaction.setPoint(point);
        // PointTransaction에 “공통 거래정보”를 기록하는 메서드
        enrollmentTransaction.recordTransaction(
                TransactionType.ENROLLMENT_INSTRUCTOR_EARN,
                amount,
                "수강 신청(" + enrollment.getCourse().getTitle() + ")"
        );

        return enrollmentTransaction;
    }
    // ----------------------------------------------------------
}
