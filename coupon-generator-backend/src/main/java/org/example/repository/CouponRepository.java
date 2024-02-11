package org.example.repository;

import org.example.entity.CouponEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends JpaRepository<CouponEntity, Integer> {

    @Query("SELECT c FROM CouponEntity c WHERE c.number = :number")
    CouponEntity getCouponEntityByNumber(@Param("number") String number);

    @Modifying
    @Query("UPDATE CouponEntity c SET c.isValid = false WHERE c.number = :number")
    void updateCouponValidity(@Param("number") String number);

    @Modifying
    @Query("UPDATE CouponEntity c SET c.usageCount = :couponUsageCount WHERE c.number = :number")
    void updateCouponUsageCount(@Param("couponUsageCount") int couponUsageCount, @Param("number") String number);

    Page<CouponEntity> findAll(Pageable pageable);
}
