package com.wanted.growthmate.payment.repository;

import com.wanted.growthmate.payment.domain.EnrollmentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentTransactionRepository extends JpaRepository<EnrollmentTransaction, Long> {
}

