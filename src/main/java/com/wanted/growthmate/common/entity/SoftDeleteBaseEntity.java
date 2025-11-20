package com.wanted.growthmate.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class SoftDeleteBaseEntity extends BaseTimeEntity {

    @Comment("삭제 일시")
    @Column(name = "deleted_at")
    protected LocalDateTime deletedAt;

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void markDeleted() {
        this.deletedAt = LocalDateTime.now();
    }

    public void markDeleted(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void restore() {
        this.deletedAt = null;
    }
}

