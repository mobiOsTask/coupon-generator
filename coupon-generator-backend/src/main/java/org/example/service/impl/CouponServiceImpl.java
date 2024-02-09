package org.example.service.impl;

import jakarta.transaction.Transactional;
import org.example.dto.CouponDTO;
import org.example.dto.CouponUserDTO;
import org.example.dto.DTO;
import org.example.entity.*;
import org.example.repository.*;
import org.example.service.CouponService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.github.curiousoddman.rgxgen.RgxGen;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    AppRepository appRepository;

    @Autowired
    CouponUserRepository couponUserRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public void createCoupon(DTO dto) {
        processData(dto);
    }

    @Transactional
    public void saveCouponEntity(List<CouponEntity> couponEntityList) {
        couponRepository.saveAll(couponEntityList);
        System.out.println(Thread.currentThread().getName());

    }

    @Transactional
    public void processData(DTO dto) {
        if (validateDTO(dto)) {
            AppEntity appEntity = createAppEntity(dto);
            appRepository.save(appEntity);

            CampaignEntity campaignEntity = createCampaignEntity(dto, appEntity);
            campaignRepository.save(campaignEntity);

            dto.getLogic().parallelStream().forEach(data -> {
                if (validateCouponDTO(data)) {
                    Set<String> strings = validateCouponNumber(data);
                    List<CouponEntity> coupons = strings.stream()
                            .map(couponNumber -> getCouponEntity(data, couponNumber, campaignEntity))
                            .collect(Collectors.toList());

                    if (!coupons.isEmpty()) {
                        saveCouponEntity(coupons);
                    }
                }
            });
        }
    }


    private AppEntity createAppEntity(DTO dto) {
        AppEntity appEntity = new AppEntity();
        appEntity.setAppId(dto.getAppId());
        appEntity.setWebSiteURL("www.testWeb.com");
        appEntity.setContactNumber("0707777777");
        return appEntity;
    }

    private CampaignEntity createCampaignEntity(DTO dto, AppEntity appEntity) {
        CampaignEntity campaignEntity = new CampaignEntity();
        campaignEntity.setAppEntity(appEntity);
        campaignEntity.setCampaignName(dto.getCampaignName());
        campaignEntity.setStartDate(dto.getStartDate());
        campaignEntity.setEndDate(dto.getEndDate());
        campaignEntity.setCouponCount(dto.getCouponCount());
        return campaignEntity;
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


    public Set<String> validateCouponNumber(CouponDTO dto) {

        if (dto.getRegex().trim().isEmpty()) {
            throw new IllegalArgumentException("Regex cannot be null, empty, or contain only whitespace");
        }
        Set<String> codeSet = new HashSet<>();
        while (codeSet.size() < dto.getCount()) {
            codeSet.add(generateCouponCode(dto.getRegex(), dto.getLength()));
        }
        return codeSet;
    }


    public String generateCouponCode(String pattern, int length) {
        String regex = pattern + "{" + length + "}$";
        RgxGen rgxGen = new RgxGen(regex);
        String generatedString = rgxGen.generate();
        return generatedString;

    }

    public boolean validateDTO(DTO dto) {
        int totalCouponCount = dto.getLogic().stream()
                .mapToInt(CouponDTO::getCount)
                .sum();

        if (totalCouponCount != dto.getCouponCount() || dto.getCouponCount() < 0) {
            throw new IllegalArgumentException("Invalid coupon count");
        }

        validateDateRange(dto.getStartDate(), dto.getEndDate());

        return true;
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            throw new IllegalArgumentException("Start date or End date cannot be empty");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start Date should be before the end date");
        }
    }

    public boolean validateCouponDTO(CouponDTO couponDto) {

        List<String> values = Arrays.asList("rs", "point", "currency");

        if (!values.contains(couponDto.getType().toLowerCase()) || couponDto.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot contain values other than 'CURRENCY' and 'POINT'");
        }


        if (!values.contains(couponDto.getDisplayValue().toLowerCase()) || couponDto.getDisplayValue().trim().isEmpty()) {
            throw new IllegalArgumentException("Display Value cannot contain values other than 'RS' and 'Point'");
        }
        return true;
    }

    @Override
    public boolean checkCoupon(String number) {
        CouponEntity couponEntity = couponRepository.getCouponEntityByNumber(number);
        boolean canUse = false;

        // check the coupon can use or not
        if ((couponEntity != null) && (couponEntity.getUsageCount() == 0 || (couponEntity.getUsageCount() == 1 && couponEntity.getIsValid()) || couponEntity.getUsageCount() > 1)) {
            canUse = true;
        }
        return canUse;
    }

    @Transactional
    @Override
    public boolean useCoupon(CouponUserDTO couponUserDTO, String number) {
        boolean isUsed = false;
        // change coupon status
        if (couponUserDTO.getUser() != null && checkCoupon(number)) {
            CouponUserEntity couponUserEntity = modelMapper.map(couponUserDTO, CouponUserEntity.class);
            Optional<UserEntity> userEntity = userRepository.findById(couponUserEntity.getUser().getUserId());

            if(userEntity.isPresent()) {
                CouponEntity couponEntity = couponRepository.getCouponEntityByNumber(number);
                // change isValid if usage count == 1
                if(couponEntity.getUsageCount() == 1){
                    isUsed = couponRepository.updateCouponValidity(number) != 0;
                // change coupon usage
                }else if (couponEntity.getUsageCount() > 1){
                    isUsed = changeCouponUsageCount(number) != 0;
                }
                couponUserRepository.save(couponUserEntity);
            }
        }
        return isUsed;
    }

    @Override
    public int changeCouponUsageCount(String number) {
        CouponEntity couponEntity = couponRepository.getCouponEntityByNumber(number);
        int couponUsageCount = couponEntity.getUsageCount();
        couponUsageCount--;
        return couponRepository.updateCouponUsageCount(couponUsageCount, number);
    }


    @Override
    public Page<CouponEntity> getCoupons(Pageable pageable) {
        return couponRepository.findAll(pageable);
    }


}
