package com.wanted.growthmate.learning.section.domain.dto;

import com.wanted.growthmate.learning.section.domain.entity.Section;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SectionSummaryResponse {
    private Long sectionId;
    private Long courseId;
    private String title;
    private Integer order;

    public static SectionSummaryResponse from(Section section) {
        return SectionSummaryResponse.builder()
                .sectionId(section.getId())
                .courseId(section.getCourseId())
                .title(section.getTitle())
                .order(section.getDisplayOrder())
                .build();
    }

}

