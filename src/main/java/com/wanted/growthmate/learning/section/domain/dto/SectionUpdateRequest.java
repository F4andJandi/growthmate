package com.wanted.growthmate.learning.section.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SectionUpdateRequest {
    @NotBlank(message = "섹션 제목은 필수 입력 값입니다.")
    private String title;

    private boolean isVisible;
}

