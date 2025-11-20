package com.wanted.growthmate.learning.course.domain.dto;

import com.wanted.growthmate.learning.course.domain.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CourseEditRequest {
    private Long categoryId;
    private String title; //변경이 없다면 기존과 같은값
    private String description;
    private Long pointAmount;
    private String imageUrl;

    public CourseEditRequest(Course course) {
        this.categoryId = course.getCategoryId();
        this.title = course.getTitle();
        this.description = course.getDescription();
        this.pointAmount = course.getPointAmount();
        this.imageUrl = course.getImageUrl();
    }

    public static CourseEditRequest from(Course course) {
        return new CourseEditRequest(course);
    }
}
