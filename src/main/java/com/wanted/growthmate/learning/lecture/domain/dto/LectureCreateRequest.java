package com.wanted.growthmate.learning.lecture.domain.dto;

import com.wanted.growthmate.learning.lecture.domain.entity.Lecture;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LectureCreateRequest {

    @NotNull(message = "courseId는 필수 입력 값입니다.")
    private Long courseId;

    @NotNull(message = "sectionId는 필수 입력 값입니다.")
    private Long sectionId;

    @NotBlank(message = "강의 제목은 필수 입력 값입니다.")
    private String title;

    @NotNull(message = "duration은 필수 입력 값입니다.")
    @PositiveOrZero(message = "duration은 0 이상이어야 합니다.")
    private Long duration;

    @NotNull(message = "order는 필수 입력 값입니다.")
    @Positive(message = "order는 1 이상의 정수여야 합니다.")
    private Integer order;

    @NotNull(message = "mediaId는 필수 입력 값입니다.")
    @Positive(message = "mediaId는 1 이상의 정수여야 합니다.")
    private Long mediaId;

    @Builder.Default
    private boolean isVisible = true;

    public Lecture toEntity() {
        return Lecture.builder()
                .courseId(courseId)
                .sectionId(sectionId)
                .title(title)
                .duration(duration)
                .mediaId(mediaId)
                .displayOrder(order)
                .isVisible(isVisible)
                .build();
    }


}
