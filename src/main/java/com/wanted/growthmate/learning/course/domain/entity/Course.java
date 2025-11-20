package com.wanted.growthmate.learning.course.domain.entity;

import com.wanted.growthmate.common.entity.SoftDeleteBaseEntity;
import com.wanted.growthmate.learning.course.domain.model.CourseEdit;
import com.wanted.growthmate.learning.course.domain.model.CourseState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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

    // 발행전이면 수정할 수 없다.
    private void verifyNotPublished() {
        if (courseState == CourseState.PUBLISHED) {
            throw new IllegalStateException("이미 발행된 강좌는 수정할 수 없습니다. ");
        }
    }

    // 강좌를 수정한다.
    public void editCourse(CourseEdit courseEdit) {
        verifyNotPublished();
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