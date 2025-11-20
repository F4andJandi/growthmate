package com.wanted.growthmate.learning.section.domain.entity;

import com.wanted.growthmate.common.entity.SoftDeleteBaseEntity;
import com.wanted.growthmate.learning.section.domain.dto.SectionUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "section")
@Getter
@Builder
@AllArgsConstructor
public class Section extends SoftDeleteBaseEntity {

    @Column(name = "course_id", nullable = false)
    @Comment("강좌 ID")
    private Long courseId;

    @Column(nullable = false)
    @Comment("섹션 제목 (챕터명)")
    private String title;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    @Comment("표시 순서 (낮을수록 먼저 표시)")
    private int displayOrder = 0;

    @Column(name = "is_visible", nullable = false)
    @Builder.Default
    @Comment("노출 여부")
    private boolean isVisible = true;

    public Section() {
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public void updateInfo(SectionUpdateRequest sectionUpdateRequest) {
        this.title = sectionUpdateRequest.getTitle();
        this.isVisible = sectionUpdateRequest.isVisible();
    }
}
