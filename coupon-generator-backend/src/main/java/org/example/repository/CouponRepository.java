package org.example.repository;

import org.example.entity.CouponEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface CouponRepository extends JpaRepository<CouponEntity, Integer> {

    @Query("SELECT c FROM CouponEntity c WHERE c.number = :number")
    CouponEntity getCouponEntityByNumber(@Param("number") String number);

    @Modifying
    @Query("UPDATE CouponEntity c SET c.isRedeemable = false WHERE c.number = :number")
    void updateCouponValidity(@Param("number") String number);

    @Modifying
    @Query("UPDATE CouponEntity c SET c.logicEntity.usageCount = :couponUsageCount WHERE c.number = :number")
    void updateCouponUsageCount(@Param("couponUsageCount") int couponUsageCount, @Param("number") String number);

    @Query("select distinct u from CouponEntity u " +
            " where (:fromDate is null or u.createdDatetime >= :fromDate) " +
            " and (:toDate is null or u.createdDatetime <= :toDate)" +
            " and (:searchEnabled is null or (u.number like concat(concat('%', :val), '%')))" +
            " or (:minAmount is null or u.logicEntity.amount >= :minAmount)" +
            " and (:maxAmount is null or u.logicEntity.amount <= :maxAmount)" +
            " or (:type is null or u.logicEntity.type = :type)" +
            " and (:displayValue is null or u.logicEntity.displayValue = :displayValue)")
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

    @Query("SELECT c FROM CouponEntity c WHERE c.logicEntity.campaignEntity.campaignId =:campaignId")
    Page<CouponEntity> getCouponEntitiesByCampaignId(@Param("campaignId") int campaignId, Pageable pageable);

    @Query("SELECT c FROM CouponEntity c WHERE c.logicEntity.campaignEntity.campaignId =:campaignId AND c.isRedeemable = true")
    Page<CouponEntity> getRedeemableCouponEntitiesByCampaignId(@Param("campaignId") int campaignId, Pageable pageable);

    @Query("SELECT c.logicEntity.campaignEntity.appEntity.appId FROM CouponEntity c WHERE c.number=:number")
    int getAppByCouponNumber(@Param("number") String number);

    @Query("SELECT c.logicEntity.campaignEntity.campaignId FROM CouponEntity c WHERE c.number=:number")
    int getCampaignByCouponNumber(@Param("number") String number);
}
