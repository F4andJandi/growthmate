package com.wanted.growthmate.enrollment.repository;

import com.wanted.growthmate.enrollment.entity.Enrollment;
import com.wanted.growthmate.enrollment.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository("enrollment_repository")
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // 단일 강좌 조회 optional로 null 확인
    Optional<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId);

    // orderNum 자동 증가를 위한 메서드
    Enrollment findTopByUserIdOrderByOrderNumDesc(Long userId);

    // 순서 정렬하여
    List<Enrollment> findEnrollmentsByUserIdAndStatusOrderByOrderNumAsc(Long userId, Status status);

}
