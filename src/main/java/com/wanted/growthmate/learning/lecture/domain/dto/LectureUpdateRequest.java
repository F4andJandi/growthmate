package com.wanted.growthmate.learning.lecture.domain.dto;

import com.wanted.growthmate.learning.lecture.domain.entity.Lecture;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LectureUpdateRequest {
    @NotNull(message = "sectionId는 필수 입력 값입니다.")
    private Long sectionId;

    @NotBlank(message = "강의 제목은 필수 입력 값입니다.")
    private String title;

    @NotNull(message = "duration은 필수 입력 값입니다.")
    @PositiveOrZero(message = "duration은 0 이상이어야 합니다.")
    private Long duration;

    @NotNull(message = "mediaId는 필수 입력 값입니다.")
    @Positive(message = "mediaId는 1 이상의 정수여야 합니다.")
    private Long mediaId;

    private Boolean isVisible;

}
