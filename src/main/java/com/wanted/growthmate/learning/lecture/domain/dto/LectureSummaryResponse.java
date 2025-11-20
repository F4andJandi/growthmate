package com.wanted.growthmate.learning.lecture.domain.dto;

import com.wanted.growthmate.learning.lecture.domain.entity.Lecture;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LectureSummaryResponse {
    private Long lectureId;
    private Long courseId;
    private Long sectionId;
    private String title;
    private Long duration;
    private Long mediaId;
    private Integer order;
    private boolean isVisible;

    public boolean isVisible() {
        return isVisible;
    }

    public static LectureSummaryResponse from(Lecture lecture) {
        return LectureSummaryResponse.builder()
                .lectureId(lecture.getId())
                .courseId(lecture.getCourseId())
                .sectionId(lecture.getSectionId())
                .title(lecture.getTitle())
                .duration(lecture.getDuration())
                .mediaId(lecture.getMediaId())
                .order(lecture.getDisplayOrder())
                .isVisible(lecture.isVisible())
                .build();
    }

}
