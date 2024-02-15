package org.example.service;

import org.example.entity.CampaignEntity;
import org.example.entity.CouponEntity;

import java.util.List;

public interface CouponStorageService {
    void saveCoupons(List<CouponEntity> couponEntityList);
    void saveCampaign(CampaignEntity campaignEntity);
}
