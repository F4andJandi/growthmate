package com.wanted.growthmate.learning.course.domain.entity;

import com.wanted.growthmate.common.entity.BaseEntity;
import com.wanted.growthmate.common.entity.BaseTimeEntity;
import com.wanted.growthmate.common.entity.SoftDeleteBaseEntity;
import com.wanted.growthmate.learning.course.domain.model.CourseEdit;
import com.wanted.growthmate.learning.course.domain.model.CourseState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor //new Course가 완전히 없어지면 없애도됨
@Entity
@Table(name = "course")
public class Course extends SoftDeleteBaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private String description;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 강사ID

    @Column(name = "point_amount", nullable = false)
    private Long pointAmount;

    @Column(name = "course_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseState courseState;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
    
    public Course() {}

    public void editCourse(CourseEdit courseEdit) {
        if (courseEdit.getTitle() != null) {
            this.title = courseEdit.getTitle();
        }
        if (courseEdit.getDescription() != null) {
            this.description = courseEdit.getDescription();
        }
        if (courseEdit.getImageUrl() != null) {
            this.imageUrl = courseEdit.getImageUrl();
        }
        this.pointAmount = courseEdit.getPointAmount();
    }
}
