package org.example.repository;

import org.example.entity.CouponEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CouponRepository extends JpaRepository<CouponEntity, Integer> {

    @Query("SELECT c FROM CouponEntity c WHERE c.number = :number")
    CouponEntity getCouponEntityByNumber(@Param("number") String number);

    @Modifying
    @Query("UPDATE CouponEntity c SET c.isValid = false WHERE c.number = :number")
    void updateCouponValidity(@Param("number") String number);

    @Modifying
    @Query("UPDATE CouponEntity c SET c.usageCount = :couponUsageCount WHERE c.number = :number")
    void updateCouponUsageCount(@Param("couponUsageCount") int couponUsageCount, @Param("number") String number);

    @Query("select distinct u from CouponEntity u " +
            " where (:fromDate is null or u.createdDatetime >= :fromDate) " +
            " and (:toDate is null or u.createdDatetime <= :toDate)" +
            " and (:searchEnabled is null or (u.number like concat(concat('%', :val), '%')))" +
            " or (:minAmount is null or u.amount >= :minAmount)" +
            " and (:maxAmount is null or u.amount <= :maxAmount)" +
            " or (:type is null or u.type = :type)" +
            " and (:displayValue is null or u.displayValue = :displayValue)")
    Page<CouponEntity> getAll(
            @Param("val") String val,
            @Param("searchEnabled") String searchEnabled,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount,
            @Param("type") String type,
            @Param("displayValue") String displayValue,
            Pageable pageable);





}
