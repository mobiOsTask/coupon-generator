package org.example.repository;

import org.example.entity.CouponUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponUserRepository extends JpaRepository<CouponUserEntity, Integer> {
}
