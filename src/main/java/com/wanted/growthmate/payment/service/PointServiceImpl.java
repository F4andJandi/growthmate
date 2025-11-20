package com.wanted.growthmate.payment.service;

import com.wanted.growthmate.enrollment.entity.Enrollment;
import com.wanted.growthmate.payment.domain.EnrollmentTransaction;
import com.wanted.growthmate.payment.domain.Point;
import com.wanted.growthmate.payment.exception.InsufficientPointBalanceException;
import com.wanted.growthmate.payment.exception.PointNotFoundException;
import com.wanted.growthmate.payment.repository.EnrollmentTransactionRepository;
import com.wanted.growthmate.payment.repository.PointRepository;
import com.wanted.growthmate.user.entity.User;
import com.wanted.growthmate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final UserRepository userRepository;
    private final PointRepository pointRepository;
    private final EnrollmentTransactionRepository enrollmentTransactionRepository;

    /*
     * 로그인한 사용자의 현재 포인트 조회 (for 유저 도메인)
     */
    @Transactional
    public Point getOrCreatePoint(Long userId) {
        // 1. 기존 포인트 계정 조회
        Optional<Point> optionalPoint = pointRepository.findByUserId(userId);

        // 2. 존재하면 바로 반환
        if (optionalPoint.isPresent()) {
            return optionalPoint.get();
        }

        // 3. 존재하지 않으면 User를 조회
        // TODO: Custom Exception
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. userId=" + userId));

        // 4. 새 Point 생성
        // TODO: 도메인 메서드로 감싸기
        Point newPoint = new Point(user, 0);

        // 5. 저장 후 반환
        return pointRepository.save(newPoint);
    }

      /*
     * 수강신청 시 학생 포인트 차감 & 강사 포인트 적립 (for 수강 도메인)
     */
    @Transactional
    public void transferEnrollmentPoints(Enrollment enrollment) {
        Long studentId = enrollment.getUser().getId();
        Long instructorId = enrollment.getCourse().getUserId();
        int coursePrice = enrollment.getCourse().getPointAmount().intValue();

        // 1. 학생/강사 Point 원장 조회
        Point studentPoint = pointRepository.findByUserId(studentId)
                .orElseThrow(() -> new PointNotFoundException(studentId));

        Point instructorPoint = pointRepository.findByUserId(instructorId)
                .orElseThrow(() -> new PointNotFoundException(instructorId));

        // 2. 학생 포인트 잔액 검증
        if (studentPoint.getBalance() < coursePrice) {
            throw new InsufficientPointBalanceException(studentId, coursePrice);
        }

        // 3. 포인트 거래 엔티티 생성
        EnrollmentTransaction studentEnrollmentTx = EnrollmentTransaction.createForStudent(
                enrollment,
                studentPoint,
                -coursePrice
        );

        EnrollmentTransaction instructorEnrollmentTx = EnrollmentTransaction.createForInstructor(
                enrollment,
                instructorPoint,
                coursePrice
        );

        // 4. 각 거래 저장
        enrollmentTransactionRepository.save(studentEnrollmentTx);
        enrollmentTransactionRepository.save(instructorEnrollmentTx);

        // 5. 원장 잔액 업데이트
        studentPoint.subtractBalance(coursePrice);
        instructorPoint.addBalance(coursePrice);
    }
}
