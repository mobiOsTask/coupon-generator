package org.example.service.impl;

import jakarta.transaction.Transactional;
import org.example.entity.CampaignEntity;
import org.example.entity.CouponEntity;
import org.example.repository.CampaignRepository;
import org.example.repository.CouponRepository;
import org.example.service.CouponStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponStorageServiceImpl implements CouponStorageService {

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Transactional
    @Async
    public void saveCoupons(List<CouponEntity> couponEntityList){
        couponRepository.saveAll(couponEntityList);
    }

    @Override
    public void saveCampaign(CampaignEntity campaignEntity) {
        campaignRepository.save(campaignEntity);
    }
}
