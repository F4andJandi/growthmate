package com.wanted.growthmate.enrollment.service;

import com.wanted.growthmate.enrollment.dto.*;
import com.wanted.growthmate.enrollment.entity.Enrollment;
import com.wanted.growthmate.enrollment.entity.Progress;
import com.wanted.growthmate.enrollment.entity.Status;
import com.wanted.growthmate.enrollment.repository.EnrollmentRepository;
import com.wanted.growthmate.learning.course.domain.entity.Course;
import com.wanted.growthmate.learning.course.repository.CourseRepository;
import com.wanted.growthmate.payment.service.PointService;
import com.wanted.growthmate.user.entity.User;
import com.wanted.growthmate.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PointService pointService;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             UserRepository userRepository,
                             CourseRepository courseRepository,
                             PointService pointService) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.pointService = pointService;
    }

    // 수강 정보 전체 조회 (사용 계획 X)
    public List<Enrollment> findAllEnrollment() {
        return enrollmentRepository.findAll();
    }

    // 강좌의 progress 반환
    public Progress findEnrollmentByUserIdAndCourseId(Long userId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                .orElse(null);

        if (enrollment == null) {
            return Progress.NONE;
        }
        return enrollment.getProgress();
    }

    // 수강 목록 생성 (강좌 조회 및 결제와 동시에 수강 목록 생성)
    @Transactional
    public Enrollment createEnrollment(EnrollmentCreateRequest request) {
        Enrollment lastEnrollment = enrollmentRepository.findTopByUserIdOrderByOrderNumDesc(request.getUserId());
        Long newOrderNum = (lastEnrollment != null) ? lastEnrollment.getOrderNum() + 1 : 1;

        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("User not found"));
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new IllegalArgumentException("Course not found")
        );

        Enrollment enrollment = new Enrollment(user, course);
        enrollment.setOrderNum(newOrderNum);
        pointService.transferEnrollmentPoints(enrollment);

        return enrollmentRepository.save(enrollment);
    }

    // userId로 수강중인 목록 조회
    @Transactional(readOnly = true)
    public List<Enrollment> findEnrollmentByUserId(Long userId, Status status) {
        return enrollmentRepository.findEnrollmentsByUserIdAndStatusOrderByOrderNumAsc(userId,status);
    }

    // 강좌의 상태를 변경 (ACTIVE, HIDDEN, REFUNDED)
    @Transactional
    public EnrollmentResponse updateEnrollmentStatus(Long enrollmentId, Status status) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("수강 정보가 없습니다."));
        User user = enrollment.getUser();

        // status를 Refunded로 변경할 때, progress 확인 진행률이 true인 경우 변경 불가
        if (status == Status.REFUNDED) {
            if (enrollment.getProgress() != Progress.NOT_DONE) {
                throw new IllegalStateException("수강 진행 중에는 환불이 불가합니다.");
            }
            enrollment.setDeletedAt(LocalDateTime.now());
        }

        enrollment.setStatus(status);

        if (status != Status.ACTIVE) {
            List<Enrollment> affectedList = enrollmentRepository.findEnrollmentsByUserIdAndStatusOrderByOrderNumAsc(
                    user.getId(), Status.ACTIVE);
            for (Enrollment e : affectedList) {
                if (e.getOrderNum() > enrollment.getOrderNum()) {
                    e.setOrderNum(e.getOrderNum() - 1);
                }
            }
            enrollment.setOrderNum(null);
        } else if (status == Status.ACTIVE){
            Enrollment lastEnrollment = enrollmentRepository.findTopByUserIdOrderByOrderNumDesc(
                    user.getId());
            Long newOrderNum = (lastEnrollment != null) ? lastEnrollment.getOrderNum() + 1 : 1;
            enrollment.setStatus(Status.ACTIVE);
            enrollment.setOrderNum(newOrderNum);
        }

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return new EnrollmentResponse(updatedEnrollment);
    }

    @Transactional
    public EnrollmentResponse updateEnrollmentProgress(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("수강 정보가 없습니다."));

        // 진행률 수정
        enrollment.setProgress(Progress.DONE);

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return new EnrollmentResponse(updatedEnrollment);
    }

    @Transactional
    public void changeOrder(Long enrollmentId, Long newOrderNum) {
        Enrollment target = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 강좌가 없다"));

        User targetUser = target.getUser();
        Long oldOrderNum = target.getOrderNum();

        // ACTIVE 상태의 유저 강좌 조회
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByUserIdAndStatusOrderByOrderNumAsc(
                targetUser.getId(), Status.ACTIVE);

        // 강좌들의 수
        int maxOrder = enrollments.size();

        for (Enrollment e : enrollments) {
            // 동일 강좌는 나중에 처리
            if (e.getId().equals(target.getId())) {
                continue;
            }

            if (oldOrderNum < newOrderNum) {
                // 뒤로 밀기: oldOrder < newOrder
                // oldOrder+1 ~ newOrder 구간의 강좌는 -1
                if (e.getOrderNum() > oldOrderNum && e.getOrderNum() <= newOrderNum) {
                    e.setOrderNum(e.getOrderNum() - 1);
                }

            } else if (oldOrderNum > newOrderNum) {
                // 앞으로 당기기: oldOrder > newOrder
                // newOrder ~ oldOrder-1 구간의 강좌는 +1
                if (e.getOrderNum() >= newOrderNum && e.getOrderNum() < oldOrderNum) {
                    e.setOrderNum(e.getOrderNum() + 1);
                }
            }
        }
        // 마지막에 대상 강좌 순서 업데이트
        target.setOrderNum(newOrderNum);
    }
}
