package com.wanted.growthmate.payment.service;

import com.wanted.growthmate.enrollment.entity.Enrollment;
import com.wanted.growthmate.payment.domain.Point;

public interface PointService {

    // NOTE: 유저 도메인에서 사용해주세요 -------------------
    // 로그인한 사용자의 현재 포인트 조회 (원장이 없으면 생성)
    Point getOrCreatePoint(Long userId);
    // ---------------------------------------------

    // NOTE: 수강 도메인에서 사용해주세요 -------------------
    // 수강신청 시 학생 포인트 차감 & 강사 포인트 적립
    public void transferEnrollmentPoints(Enrollment enrollment);
}
