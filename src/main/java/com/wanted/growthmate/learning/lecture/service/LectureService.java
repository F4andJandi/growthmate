package com.wanted.growthmate.learning.lecture.service;

import com.wanted.growthmate.learning.lecture.domain.dto.*;

import java.util.List;

public interface LectureService {
    LectureResponse save(LectureCreateRequest lectureCreateRequest) ;

    List<LectureSummaryResponse> findByCourseId(Long courseId);

    List<LectureSummaryResponse> findBySectionId(Long sectionId) ;

    LectureResponse findByLectureId(Long lectureId);

    LectureResponse updateInfo(Long lectureId, LectureUpdateRequest request);

    LectureResponse updateOrder(Long lectureId, LectureOrderUpdateRequest request);

    void delete(Long lectureId);

    void softDelete(Long lectureId);

}
