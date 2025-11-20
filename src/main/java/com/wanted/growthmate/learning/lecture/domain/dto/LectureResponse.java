package com.wanted.growthmate.learning.lecture.domain.dto;

import com.wanted.growthmate.learning.lecture.domain.entity.Lecture;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LectureResponse {
    private Long lectureId;
    private Long courseId;
    private Long sectionId;
    private String title;
    private Long duration;
    private Integer order;
    private Long mediaId;
    private boolean isVisible;

    public static LectureResponse from(Lecture lecture) {
        return LectureResponse.builder()
                .lectureId(lecture.getId())
                .courseId(lecture.getCourseId())
                .sectionId(lecture.getSectionId())
                .title(lecture.getTitle())
                .duration(lecture.getDuration())
                .order(lecture.getDisplayOrder())
                .mediaId(lecture.getMediaId())
                .isVisible(lecture.isVisible())
                .build();
    }

}
