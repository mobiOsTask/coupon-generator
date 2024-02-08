package org.example.service.impl;

import jakarta.transaction.Transactional;
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

import java.util.*;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    AppRepository appRepository;

    public void processData(DTO dto) {

        if (validateDto(dto)) {
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
                if (validateCouponDto(data)) {
                    Set<String> strings = validateCode(data);
                    for (String couponNumber : strings) {
                        CouponEntity couponEntity = getCouponEntity(data, couponNumber, campaignEntity);

                        coupon.add(couponEntity);
                    }
                    couponRepository.saveAll(coupon);
                }

            }

        }
    }

    private CouponEntity getCouponEntity(CouponDTO data, String couponNumber, CampaignEntity campaignEntity) {
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setCampaignEntity(campaignEntity);
        couponEntity.setCount(data.getCount());
        couponEntity.setType(data.getType());
        couponEntity.setAmount(data.getAmount());
        couponEntity.setDisplayValue(data.getDisplayValue());
        couponEntity.setUsageCount(data.getUsageCount());
        couponEntity.setIsValid(data.getIsValid());
        couponEntity.setLength(data.getLength());
        couponEntity.setRegex(data.getRegex());
        couponEntity.setNumber(couponNumber);
        return couponEntity;
    }


    @Override
    public void createCoupon(DTO dto) {
        processData(dto);
    }

    public Set<String> validateCode(CouponDTO dto){

        if (dto.getRegex().trim().isEmpty()) {
            throw new IllegalArgumentException("Regex cannot be null, empty, or contain only whitespace");
        }
        Set<String> codeSet = new HashSet<>();
        while (codeSet.size() < dto.getCount()) {
            codeSet.add(randomCode(dto.getRegex(), dto.getLength()));
        }
        return codeSet;
    }

    public boolean validateDto(DTO dto) {
        int count = 0;
        for (CouponDTO data : dto.getLogic()) {
            count += data.getCount();
        }
        if (count != dto.getCouponCount() || dto.getCouponCount() < 0) {
            throw new IllegalArgumentException("Invalid coupon count");
        }

        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new IllegalArgumentException("Start date or End date cannot be empty");
        }


        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start Date should be before the end date");
        }
        return true;
    }

    public boolean validateCouponDto(CouponDTO couponDto) {

        List<String> values = Arrays.asList("rs", "point", "currency");

        if (!values.contains(couponDto.getType().toLowerCase()) || couponDto.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot contain values other than 'CURRENCY' and 'POINT'");
        }


        if (!values.contains(couponDto.getDisplayValue().toLowerCase()) || couponDto.getDisplayValue().trim().isEmpty()) {
            throw new IllegalArgumentException("Display Value cannot contain values other than 'RS' and 'Point'");
        }
        return true;
    }

    @Transactional
    @Override
    public boolean useCoupon(String number) {
        CouponEntity couponEntity = couponRepository.getCouponEntityByNumber(number);
        boolean canUse = false;
        if(couponEntity.getUsageCount() == 0 || (couponEntity.getUsageCount() == 1 && couponEntity.getIsValid())){

            //change coupon status
            if(couponRepository.updateCouponValidity(number) != 0){
                canUse = true;
            }
        }
        return canUse;
    }

    public String randomCode(String pattern, int length) {
        String regex = pattern + "{" + length + "}$";
        RgxGen rgxGen = new RgxGen(regex);
        String generatedString = rgxGen.generate();
        return generatedString;

    }

    @Override
    public List<CouponEntity> getCoupons() {
        return couponRepository.findAll();
    }


}
