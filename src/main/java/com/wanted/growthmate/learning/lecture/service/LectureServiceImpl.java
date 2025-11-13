package com.wanted.growthmate.learning.lecture.service;

import com.wanted.growthmate.learning.lecture.domain.dto.*;
import com.wanted.growthmate.learning.lecture.domain.entity.Lecture;
import com.wanted.growthmate.learning.lecture.exception.LectureNotFoundException;
import com.wanted.growthmate.learning.lecture.repository.LectureRepository;
import com.wanted.growthmate.learning.section.domain.entity.Section;
import com.wanted.growthmate.learning.section.service.SectionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final SectionService sectionService;

    public LectureServiceImpl(LectureRepository lectureRepository, SectionService sectionService) {
        this.lectureRepository = lectureRepository;
        this.sectionService = sectionService;
    }

    @Override
    public LectureResponse save(LectureCreateRequest lectureCreateRequest) {
        Section section = sectionService.getSectionById(lectureCreateRequest.getSectionId());
        
        Lecture lecture = Lecture.builder()
                .section(section)
                .title(lectureCreateRequest.getTitle())
                .duration(lectureCreateRequest.getDuration())
                .mediaId(lectureCreateRequest.getMediaId())
                .displayOrder(lectureCreateRequest.getOrder())
                .isVisible(lectureCreateRequest.isVisible())
                .build();
        
        return LectureResponse.from(
                lectureRepository.save(lecture)
        );
    }

    @Override
    public List<LectureSummaryResponse> findByCourseId(Long courseId) {
        return lectureRepository.findBySection_Course_Id(courseId).stream().map(lecture ->
                LectureSummaryResponse.from(lecture)
        ).collect(Collectors.toList());
    }

    @Override
    public List<LectureSummaryResponse> findBySectionId(Long sectionId) {
        Section section = sectionService.getSectionById(sectionId);
        return lectureRepository.findBySectionOrderByDisplayOrderAsc(section).stream().map(lecture ->
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
        
        // Section이 변경된 경우에만 조회 및 설정
        if (lectureUpdateRequest.getSectionId() != null) {
            Section section = sectionService.getSectionById(lectureUpdateRequest.getSectionId());
            lecture.changeSection(section);
        }

        lecture.updateInfo(lectureUpdateRequest);

        return LectureResponse.from(lecture);
    }

    @Override
    @Transactional
    public LectureResponse updateOrder(Long lectureId, LectureOrderUpdateRequest request) {
        Section section = sectionService.getSectionById(request.getSectionId());
        List<Lecture> lectureList = lectureRepository.findBySection(section).stream()
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
