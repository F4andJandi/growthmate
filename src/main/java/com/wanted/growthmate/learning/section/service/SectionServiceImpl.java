package com.wanted.growthmate.learning.section.service;

import com.wanted.growthmate.learning.section.domain.dto.*;
import com.wanted.growthmate.learning.section.domain.entity.Section;
import com.wanted.growthmate.learning.section.exception.SectionNotFoundException;
import com.wanted.growthmate.learning.section.repository.SectionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;

    public SectionServiceImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public SectionResponse save(SectionCreateRequest sectionCreateRequest) {
        return SectionResponse.from(
                sectionRepository.save(sectionCreateRequest.toEntity())
        );
    }

    @Override
    public List<SectionSummaryResponse> findByCourseId(Long courseId) {
        return sectionRepository.findByCourseIdOrderByDisplayOrderAsc(courseId).stream().map(section ->
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
        List<Section> sectionList = sectionRepository.findByCourseId(request.getCourseId()).stream()
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

