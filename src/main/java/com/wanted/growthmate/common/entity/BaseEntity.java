package com.wanted.growthmate.common.entity;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    public Long getId() {
        return id;
    }

    protected BaseEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        // 프록시 안전 비교
        if (org.hibernate.Hibernate.getClass(this) != org.hibernate.Hibernate.getClass(o)) return false;
        BaseEntity that = (BaseEntity) o;

        // 영속화 전(id == null)에는 '동등 아님'으로 처리
        return this.id != null && this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        // 변하지 않는 클래스 해시 사용 (프록시 안전성을 위해 Hibernate.getClass 사용해도 됨)
        return org.hibernate.Hibernate.getClass(this).hashCode();
    }
}
