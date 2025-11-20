package com.wanted.growthmate.learning.lecture.domain.entity;

import com.wanted.growthmate.common.entity.BaseEntity;
import com.wanted.growthmate.common.entity.SoftDeleteBaseEntity;
import com.wanted.growthmate.learning.lecture.domain.dto.LectureUpdateRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;


@Entity
@Table(name = "lecture")
@Builder
@AllArgsConstructor
public class Lecture extends SoftDeleteBaseEntity {

//    @ManyToOne()
    @Getter
    @Column(name = "course_id")
    @Comment("강좌 ID")
    private Long courseId;

//    @ManyToOne()
    @Getter
    @Column(name = "section_id")
    @Comment("섹션 ID")
    private Long sectionId;

    @Getter
    @Column(nullable = false)
    @NotBlank(message = "강의 제목은 필수입니다.")
    @Comment("강의 제목")
    private String title;


    @Getter
    @Column(nullable = false)
    @Comment("강의 길이")
    private Long duration;

//    @OneToMany()
    @Getter
    @Column(name = "media_id", nullable = false)
    @Comment("강의 미디어 ID")
    private Long mediaId;

    @Getter
    @Column(name = "display_order", nullable = false)
    @Comment("강의 화면 노출 순서")
    private int displayOrder;

    @Column(name = "is_visible", nullable = false)
    @Builder.Default
    @Comment("강의 노출여부")
    private boolean isVisible = true;

    public Lecture() {
    }

    public Lecture(Long courseId, Long sectionId, String title, Long duration, Long mediaId, int order) {
        this.courseId = courseId;
        this.sectionId = sectionId;
        this.title = title;
        this.duration = duration;
        this.mediaId = mediaId;
        this.displayOrder = order;
    }

    public boolean isVisible() {
        return isVisible;
    }


    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", sectionId=" + sectionId +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                ", mediaId=" + mediaId +
                ", displayOrder=" + displayOrder +
                ", isVisible=" + isVisible +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public void updateInfo(LectureUpdateRequest lectureUpdateRequest) {
        this.sectionId = lectureUpdateRequest.getSectionId();
        this.title = lectureUpdateRequest.getTitle();
        if (lectureUpdateRequest.getIsVisible() != null) {
            this.isVisible = lectureUpdateRequest.getIsVisible();
        }
        this.duration = lectureUpdateRequest.getDuration();
        this.mediaId = lectureUpdateRequest.getMediaId();
    }
}
