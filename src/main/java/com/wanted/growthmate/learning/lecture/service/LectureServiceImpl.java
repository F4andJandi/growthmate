package com.wanted.growthmate.learning.lecture.service;

import com.wanted.growthmate.learning.lecture.domain.dto.*;
import com.wanted.growthmate.learning.lecture.domain.entity.Lecture;
import com.wanted.growthmate.learning.lecture.exception.LectureNotFoundException;
import com.wanted.growthmate.learning.lecture.repository.LectureRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;

    public LectureServiceImpl(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    public LectureResponse save(LectureCreateRequest lectureCreateRequest) {
        return LectureResponse.from(
                lectureRepository.save(lectureCreateRequest.toEntity())
        );
    }

    @Override
    public List<LectureSummaryResponse> findByCourseId(Long courseId) {
        return lectureRepository.findByCourseId(courseId).stream().map(lecture ->
                LectureSummaryResponse.from(lecture)
        ).collect(Collectors.toList());
    }

    @Override
    public List<LectureSummaryResponse> findBySectionId(Long sectionId) {
        return lectureRepository.findBySectionIdOrderByDisplayOrderAsc(sectionId).stream().map(lecture ->
                LectureSummaryResponse.from(lecture)
        ).collect(Collectors.toList());
    }

    @Override
    public LectureResponse findByLectureId(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException(lectureId));
        return LectureResponse.from(lecture);
    }

    @Override
    @Transactional
    public LectureResponse updateInfo(Long lectureId, LectureUpdateRequest lectureUpdateRequest) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException(lectureId));

        lecture.updateInfo(lectureUpdateRequest);

        return LectureResponse.from(lecture);
    }

    @Override
    @Transactional
    public LectureResponse updateOrder(Long lectureId, LectureOrderUpdateRequest request) {
        List<Lecture> lectureList = lectureRepository.findBySectionId(request.getSectionId()).stream()
                .sorted(Comparator.comparing(Lecture::getDisplayOrder))
                .toList();

        Lecture beforeLecture = null;
        Lecture afterLecture = null;

        for (Lecture lecture : lectureList) {
            if (Objects.equals(lecture.getId(), lectureId)) 
                beforeLecture = lecture;
            
            if (Objects.equals(lecture.getDisplayOrder(), request.getNewOrder())) 
                afterLecture = lecture;
            
            if (beforeLecture != null && afterLecture != null) 
                break;
        }

        if (beforeLecture == null || afterLecture == null) {
            throw new IllegalArgumentException("대상 강의 또는 교환 대상 강의를 찾을 수 없습니다.");
        }

        // 순서 교환
        int order = beforeLecture.getDisplayOrder();
        beforeLecture.setDisplayOrder(afterLecture.getDisplayOrder());
        afterLecture.setDisplayOrder(order);

        return LectureResponse.from(beforeLecture);

    }

    @Override
    @Transactional
    public void delete(Long lectureId) {
        if (!lectureRepository.existsById(lectureId))
            throw new LectureNotFoundException(lectureId);
        
        lectureRepository.deleteById(lectureId);
    }

    @Override
    @Transactional
    public void softDelete(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException(lectureId));
        lecture.markDeleted();
    }

}
