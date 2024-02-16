package org.example.repository;

import org.example.entity.CampaignEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampaignRepository extends JpaRepository<CampaignEntity,Integer> {

    @Query("SELECT c FROM CampaignEntity c WHERE c.appEntity.appId =:appId")
    Page<CampaignEntity> getCampaignEntitiesByAppId(@Param("appId") int appId, Pageable pageable);

    @Query("SELECT c FROM CampaignEntity c WHERE c.appEntity.appId =:appId AND CURRENT_DATE BETWEEN c.startDate AND c.endDate")
    Page<CampaignEntity> getValidCampaignEntitiesByAppId(@Param("appId") int appId, Pageable pageable);
}
