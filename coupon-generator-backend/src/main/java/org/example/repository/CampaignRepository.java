package org.example.repository;

import org.example.entity.CampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<CampaignEntity,Integer> {
}
