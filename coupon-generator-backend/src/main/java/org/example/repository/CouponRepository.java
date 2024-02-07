package org.example.repository;

import org.example.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends JpaRepository<CouponEntity, Integer> {

    @Query("SELECT c FROM CouponEntity c WHERE c.number = :number")
    CouponEntity getCouponEntityByNumber(@Param("number") String number);
    @Modifying
    @Query("UPDATE CouponEntity c SET c.isValid = false WHERE c.number = :number")
    int updateCouponValidity(@Param("number") String number);
}
