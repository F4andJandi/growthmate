package com.wanted.growthmate.payment.service;

import com.wanted.growthmate.payment.domain.Point;
import com.wanted.growthmate.payment.dto.PointTransactionSummary;

import java.util.List;

public interface PointTransactionService {

    // 로그인한 사용자의 포인트 전체 내역 조회
    List<PointTransactionSummary> getTransactionSummaries(Point point);
}
