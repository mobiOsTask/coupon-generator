package org.example.service.impl;

import org.example.dto.CouponDTO;
import org.example.dto.DTO;
import org.example.entity.AppEntity;
import org.example.entity.CampaignEntity;
import org.example.entity.CouponEntity;
import org.example.repository.AppRepository;
import org.example.repository.CampaignRepository;
import org.example.repository.CouponRepository;
import org.example.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.curiousoddman.rgxgen.RgxGen;

import java.util.ArrayList;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    AppRepository appRepository;

    @Override
    public void createCoupon(DTO dto) {
        AppEntity appEntity = new AppEntity();
        appEntity.setAppId(dto.getAppId());
        appEntity.setWebSiteURL("www.testWeb.com");
        appEntity.setContactNumber("0707777777");
        appRepository.save(appEntity);


        CampaignEntity campaignEntity = new CampaignEntity();
        campaignEntity.setAppEntity(appEntity);
        campaignEntity.setCampaignName(dto.getCampaignName());
        campaignEntity.setStartDate(dto.getStartDate());
        campaignEntity.setEndDate(dto.getEndDate());
        campaignEntity.setCouponCount(dto.getCouponCount());
        campaignRepository.save(campaignEntity);


        for (CouponDTO data : dto.getLogic()) {
            List<CouponEntity> coupon = new ArrayList<>();
            for (int i = 0; i < data.getCount(); i++) {
                CouponEntity couponEntity = new CouponEntity();
                couponEntity.setCampaignEntity(campaignEntity);
                couponEntity.setCount(data.getCount());
                couponEntity.setType(data.getType());
                couponEntity.setAmount(data.getAmount());
                couponEntity.setDisplayValue(data.getDisplayValue());
                couponEntity.setUsageCount(data.isUsageCount());
                couponEntity.setLength(data.getLength());
                couponEntity.setRegex(data.getRegex());
                couponEntity.setNumber(randomCode(data.getRegex(), data.getLength()));

                coupon.add(couponEntity);
            }
            couponRepository.saveAll(coupon);
        }
    }

    @Override
    public List<CouponEntity> getCoupons() {
        return couponRepository.findAll();
    }

    public String randomCode(String pattern, int length) {
        String regex = pattern + "{" + length + "}$";
        RgxGen rgxGen = new RgxGen(regex);
        String generatedString = rgxGen.generate();
        return generatedString;

    }
}
