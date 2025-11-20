package com.wanted.growthmate.payment.domain;

import com.wanted.growthmate.common.entity.BaseTimeEntity;
import com.wanted.growthmate.payment.exception.InsufficientPointBalanceException;
import com.wanted.growthmate.payment.exception.InvalidPointAmountException;
import com.wanted.growthmate.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "points")
@Comment("원장 - 사용자별 현재 포인트 보유량")
public class Point extends BaseTimeEntity {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "balance", nullable = false)
    @Comment("현재 포인트 잔액")
    private Integer balance;

    protected Point() {}

    public Point(User user, Integer balance) {
        this.user = user;
        this.balance = balance;
    }

    /*
     * 포인트 적립
     */
    public void addBalance(int amount) {
        if (amount <= 0) {
            throw new InvalidPointAmountException(amount);
        }
        this.balance += amount;
    }

    // 포인트 차감
    public void subtractBalance(int amount) {
        if (amount <= 0) {
            throw new InvalidPointAmountException(amount);
        }

        if (this.balance < amount) {
            throw new InsufficientPointBalanceException(user.getId(), amount);
        }

        this.balance -= amount;
    }
}
