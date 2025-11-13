package com.wanted.growthmate.learning.lecture.domain.entity;

import com.wanted.growthmate.common.entity.SoftDeleteBaseEntity;
import com.wanted.growthmate.learning.lecture.domain.dto.LectureUpdateRequest;
import com.wanted.growthmate.learning.section.domain.entity.Section;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;


@Entity
@Table(name = "lecture")
@Builder
@AllArgsConstructor
public class Lecture extends SoftDeleteBaseEntity {

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    @Comment("섹션")
    private Section section;

    @Getter
    @Column(nullable = false)
    @NotBlank(message = "강의 제목은 필수입니다.")
    @Comment("강의 제목")
    private String title;

    @Getter
    @Column(nullable = false)
    @Comment("강의 길이")
    private Long duration;

    @Getter
    @Column(name = "media_id", nullable = false)
    @Comment("강의 미디어 ID")
    private Long mediaId;

    @Setter
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

    public Lecture(Section section, String title, Long duration, Long mediaId, int order) {
        this.section = section;
        this.title = title;
        this.duration = duration;
        this.mediaId = mediaId;
        this.displayOrder = order;
    }

    public boolean isVisible() {
        return isVisible;
    }


    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", section=" + section.toString() +
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
        this.title = lectureUpdateRequest.getTitle();
        if (lectureUpdateRequest.getIsVisible() != null) {
            this.isVisible = lectureUpdateRequest.getIsVisible();
        }
        this.duration = lectureUpdateRequest.getDuration();
        this.mediaId = lectureUpdateRequest.getMediaId();
    }

    public void changeSection(Section section) {
        this.section = section;
    }
}
