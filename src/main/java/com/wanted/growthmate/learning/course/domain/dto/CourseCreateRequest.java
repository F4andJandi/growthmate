package com.wanted.growthmate.learning.course.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // 바인딩 위함
@Builder
@AllArgsConstructor // 테스트에서 필요함.
public class CourseCreateRequest {
    private String title;
    private Long categoryId;
    private String description;
    private Long userId; // 강사ID
    private Long pointAmount;
    private String imageUrl;

    public CourseCreateRequest() {}

}
