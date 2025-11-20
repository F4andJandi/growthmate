package com.wanted.growthmate.learning.section.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SectionOrderUpdateRequest {
    @NotNull(message = "order는 필수 입력 값입니다.")
    @PositiveOrZero(message = "order는 0 이상이어야 합니다.")
    private Integer newOrder;

    @NotNull(message = "강좌 ID는 필수입니다.")
    private Long courseId;

}

