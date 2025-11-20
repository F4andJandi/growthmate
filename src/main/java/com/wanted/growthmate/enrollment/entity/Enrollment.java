package com.wanted.growthmate.enrollment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wanted.growthmate.learning.course.domain.entity.Course;
import com.wanted.growthmate.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "order_num")
    private Long orderNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "enrollment_status", nullable = false)
    private Status status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "progress", nullable = false)
    private Progress progress;

    protected Enrollment() {}

    public Enrollment(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    // 데이터 저장 전 자동으로 생성일 세팅
    @PrePersist
    public void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.progress == null) {
            this.progress = Progress.NOT_DONE;
        }
        if (this.status == null) {
            this.status = Status.ACTIVE;
        }
    }

    public Long getId() { return id; }

    public User getUser() { return user; }

    public Course getCourse() { return course; }

    public Status getStatus() { return status; }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }

    public Progress getProgress() { return progress; }

    public void setProgress(Progress progress) { this.progress = progress; }

    public Long getOrderNum() { return orderNum; }

    public void setOrderNum(Long orderNum) { this.orderNum = orderNum; }
}
