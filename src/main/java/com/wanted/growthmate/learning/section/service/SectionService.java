package com.wanted.growthmate.learning.section.service;

import com.wanted.growthmate.learning.section.domain.dto.*;

import java.util.List;

public interface SectionService {
    SectionResponse save(SectionCreateRequest sectionCreateRequest);

    List<SectionSummaryResponse> findByCourseId(Long courseId);

    SectionResponse findBySectionId(Long sectionId);

    SectionResponse updateInfo(Long sectionId, SectionUpdateRequest request);

    SectionResponse updateOrder(Long sectionId, SectionOrderUpdateRequest request);

    void delete(Long sectionId);

    void softDelete(Long sectionId);
}

