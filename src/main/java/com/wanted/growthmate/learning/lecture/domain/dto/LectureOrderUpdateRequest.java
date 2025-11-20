package com.wanted.growthmate.learning.lecture.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LectureOrderUpdateRequest {
    @NotNull(message = "order는 필수 입력 값입니다.")
    @Positive(message = "order는 1 이상의 정수여야 합니다.")
    private Integer newOrder;

    @NotNull(message = "섹션 ID는 필수입니다.")
    private Long sectionId;

}
