package com.wanted.growthmate.learning.section.service;

import com.wanted.growthmate.learning.course.domain.entity.Course;
import com.wanted.growthmate.learning.course.service.CourseService;
import com.wanted.growthmate.learning.section.domain.dto.*;
import com.wanted.growthmate.learning.section.domain.entity.Section;
import com.wanted.growthmate.learning.section.exception.SectionNotFoundException;
import com.wanted.growthmate.learning.section.repository.SectionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;
    private final CourseService courseService;

    public SectionServiceImpl(SectionRepository sectionRepository, CourseService courseService) {
        this.sectionRepository = sectionRepository;
        this.courseService = courseService;
    }

    @Override
    public SectionResponse save(SectionCreateRequest sectionCreateRequest) {
        Course course = courseService.getCourse(sectionCreateRequest.getCourseId())
                .orElseThrow(() -> new NoSuchElementException("아이디에 해당하는 코스가 존재하지 않습니다."));
        
        Section section = Section.builder()
                .course(course)
                .title(sectionCreateRequest.getTitle())
                .displayOrder(sectionCreateRequest.getOrder())
                .isVisible(sectionCreateRequest.isVisible())
                .build();
        
        return SectionResponse.from(
                sectionRepository.save(section)
        );
    }

    @Override
    public List<SectionSummaryResponse> findByCourseId(Long courseId) {
        return sectionRepository.findByCourse_IdOrderByDisplayOrderAsc(courseId).stream().map(section ->
                SectionSummaryResponse.from(section)
        ).collect(Collectors.toList());
    }

    @Override
    public SectionResponse findBySectionId(Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new SectionNotFoundException(sectionId));
        return SectionResponse.from(section);
    }

    @Override
    public Section getSectionById(Long sectionId) {
        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new SectionNotFoundException(sectionId));
    }

    @Override
    @Transactional
    public SectionResponse updateInfo(Long sectionId, SectionUpdateRequest sectionUpdateRequest) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new SectionNotFoundException(sectionId));

        section.updateInfo(sectionUpdateRequest);

        return SectionResponse.from(section);
    }

    @Override
    @Transactional
    public SectionResponse updateOrder(Long sectionId, SectionOrderUpdateRequest request) {
        List<Section> sectionList = sectionRepository.findByCourse_Id(request.getCourseId()).stream()
                .sorted(Comparator.comparing(Section::getDisplayOrder))
                .toList();

        Section beforeSection = null;
        Section afterSection = null;

        for (Section section : sectionList) {
            if (Objects.equals(section.getId(), sectionId))
                beforeSection = section;

            if (Objects.equals(section.getDisplayOrder(), request.getNewOrder()))
                afterSection = section;

            if (beforeSection != null && afterSection != null)
                break;
        }

        if (beforeSection == null || afterSection == null) {
            throw new IllegalArgumentException("대상 섹션 또는 교환 대상 섹션을 찾을 수 없습니다.");
        }

        // 순서 교환
        int order = beforeSection.getDisplayOrder();
        beforeSection.setDisplayOrder(afterSection.getDisplayOrder());
        afterSection.setDisplayOrder(order);

        return SectionResponse.from(beforeSection);
    }

    @Override
    @Transactional
    public void delete(Long sectionId) {
        if (!sectionRepository.existsById(sectionId))
            throw new SectionNotFoundException(sectionId);

        sectionRepository.deleteById(sectionId);
    }

    @Override
    @Transactional
    public void softDelete(Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new SectionNotFoundException(sectionId));
        section.markDeleted();
    }
}

