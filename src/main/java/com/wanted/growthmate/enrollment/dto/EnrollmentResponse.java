package com.wanted.growthmate.enrollment.dto;

import com.wanted.growthmate.enrollment.entity.Enrollment;

public class EnrollmentResponse {

    private Long id;
    private Long userId;
    private Long courseId;
    private String status;
    private String progress;
    private Long orderNum;

    public EnrollmentResponse(Enrollment e) {
        this.id = e.getId();
        this.userId = e.getUser().getId();
        this.courseId = e.getCourse().getId();
        this.status = e.getStatus().name();
        this.progress = e.getProgress().name();
        this.orderNum = e.getOrderNum();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getStatus() {
        return status;
    }

    public String getProgress() {
        return progress;
    }

    public Long getOrderNum() {
        return orderNum;
    }
}
