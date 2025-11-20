package com.wanted.growthmate.enrollment.dto;

import jakarta.validation.constraints.NotNull;

public class EnrollmentCreateRequest {

    @NotNull(message = "userId는 반드시 필요합니다.")
    private Long userId;

    @NotNull(message = "courseId는 반드시 필요합니다.")
    private Long courseId;

    public EnrollmentCreateRequest(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
