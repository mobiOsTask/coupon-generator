package org.example.repository;

import org.example.entity.CouponEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface CouponRepository extends JpaRepository<CouponEntity, Integer> {

    @Query("SELECT c FROM CouponEntity c WHERE c.number = :number")
    CouponEntity getCouponEntityByNumber(@Param("number") String number);

    @Modifying
    @Query("UPDATE CouponEntity c SET c.isRedeemable = false WHERE c.number = :number")
    void updateCouponValidity(@Param("number") String number);

    @Modifying
    @Query("UPDATE CouponEntity c SET c.usageCount = :couponUsageCount WHERE c.number = :number")
    void updateCouponUsageCount(@Param("couponUsageCount") int couponUsageCount, @Param("number") String number);

    @Query("select u from CouponEntity u " +
            "where (:dateFrom is null or u .logicEntity.startDate >= :dateFrom) " +
            "and (:dateTo is null or u .logicEntity.endDate <= :dateTo)" +
            "and (:searchEnabled is null or (u.number like concat(concat('%', :val), '%')))" +
            "and (:minAmount is null or u.logicEntity.amount >= :minAmount)" +
            "and (:maxAmount = 0 or u.logicEntity.amount <= :maxAmount)" +
            "and (:type is null or u.logicEntity.type = :type)" +
            "and (:displayValue is null or u.logicEntity.displayValue = :displayValue)")
    Page<CouponEntity> getAll(
            Pageable pageable,
            @Param("val") String val,
            @Param("searchEnabled") String searchEnabled,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount,
            @Param("type") String type,
            @Param("displayValue") String displayValue);

    @Query("select count(u) from CouponEntity u " +
            "where (:dateFrom is null or u.logicEntity.startDate >= :dateFrom) " +
            "and (:dateTo is null or u.logicEntity.endDate <= :dateTo)" +
            "and (:searchEnabled is null or (u.number like concat(concat('%', :val), '%')))" +
            "and (u.isRedeemable = :is_redeemable)" +
            "and (:minAmount is null or u.logicEntity.amount >= :minAmount)" +
            "and (:maxAmount = 0 or u.logicEntity.amount <= :maxAmount)" +
            "and (:type is null or u.logicEntity.type = :type)" +
            "and (:displayValue is null or u.logicEntity.displayValue = :displayValue)")
    int getAllCounts(
            @Param("val") String val,
            @Param("searchEnabled") String searchEnabled,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount,
            @Param("type") String type,
            @Param("displayValue") String displayValue,
            @Param("is_redeemable") boolean is_redeemable);


    @Query("SELECT u FROM CouponEntity u WHERE (u.isRedeemable = :is_redeemable)")
    Page<CouponEntity> getRedeemableCoupons(Pageable pageable, @Param("is_redeemable") boolean is_redeemable);


    @Query("SELECT c FROM CouponEntity c WHERE c.logicEntity.campaignEntity.campaignId =:campaignId")
    Page<CouponEntity> getCouponEntitiesByCampaignId(@Param("campaignId") int campaignId, Pageable pageable);

    @Query("SELECT c FROM CouponEntity c WHERE c.logicEntity.campaignEntity.campaignId =:campaignId AND c.isRedeemable = true")
    Page<CouponEntity> getRedeemableCouponEntitiesByCampaignId(@Param("campaignId") int campaignId, Pageable pageable);

    @Query("SELECT c.logicEntity.campaignEntity.appEntity.appId FROM CouponEntity c WHERE c.number=:number")
    int getAppByCouponNumber(@Param("number") String number);

    @Query("SELECT c.logicEntity.campaignEntity.campaignId FROM CouponEntity c WHERE c.number=:number")
    int getCampaignByCouponNumber(@Param("number") String number);

}
