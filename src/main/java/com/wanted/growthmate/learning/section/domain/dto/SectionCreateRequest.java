package com.wanted.growthmate.learning.section.domain.dto;

import com.wanted.growthmate.learning.section.domain.entity.Section;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SectionCreateRequest {

    @NotNull(message = "courseId는 필수 입력 값입니다.")
    private Long courseId;

    @NotBlank(message = "섹션 제목은 필수 입력 값입니다.")
    private String title;

    @NotNull(message = "order는 필수 입력 값입니다.")
    @PositiveOrZero(message = "order는 0 이상이어야 합니다.")
    private Integer order;

    @Builder.Default
    private boolean isVisible = true;

    public Section toEntity() {
        return Section.builder()
                .courseId(courseId)
                .title(title)
                .displayOrder(order)
                .isVisible(isVisible)
                .build();
    }

}

