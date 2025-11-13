package com.wanted.growthmate.learning.course.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // 바인딩 위함
@Builder
@AllArgsConstructor // 테스트에서 필요함.
public class CourseCreateRequest {
    @NotNull(message = "title은 필수 입력 값입니다.")
    private String title;

    @NotNull(message = "categoryId는 필수 입력 값입니다.")
    private Long categoryId;
    private String description;
    private Long userId; // 강사ID

    @NotNull(message = "pointAmount는 필수 입력 값입니다.")
    @Positive(message = "pointAmount는 0보다 큰 값이어야 합니다.")
    private Long pointAmount;
    private String imageUrl;

    public CourseCreateRequest() {}
}
