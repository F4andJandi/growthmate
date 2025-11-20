package com.wanted.growthmate.learning.section.domain.dto;

import com.wanted.growthmate.learning.section.domain.entity.Section;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SectionResponse {
    private Long sectionId;
    private Long courseId;
    private String title;
    private Integer order;
    private boolean isVisible;

    public static SectionResponse from(Section section) {
        return SectionResponse.builder()
                .sectionId(section.getId())
                .courseId(section.getCourseId())
                .title(section.getTitle())
                .order(section.getDisplayOrder())
                .isVisible(section.isVisible())
                .build();
    }

}

