package com.wanted.growthmate.learning.course.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseEdit {
    private String title; //변경이 없다면 기존과 같은값
    private String description;
    private Long pointAmount;
    private String imageUrl;
}
