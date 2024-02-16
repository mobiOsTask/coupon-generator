package org.example.repository;

import org.example.entity.CouponEntity;
import org.example.entity.CouponUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponUserRepository extends JpaRepository<CouponUserEntity, Integer> {
    @Query("select c from CouponUserEntity c where c.coupon.number = :couponNumber")
    Page<CouponUserEntity> getCouponData(Pageable pageRequest, @Param("couponNumber") String couponNumber);

}
