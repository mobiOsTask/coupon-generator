package org.example.service.impl;

import jakarta.transaction.Transactional;
import org.example.dto.ApiResponse;
import org.example.dto.CouponDTO;
import org.example.dto.CouponUserDTO;
import org.example.dto.DTO;
import org.example.entity.*;
import org.example.repository.*;
import org.example.entity.AppEntity;
import org.example.entity.CampaignEntity;
import org.example.entity.CouponEntity;
import org.example.entity.CouponUserEntity;
import org.example.exception.DLAppValidationsException;
import org.example.repository.AppRepository;
import org.example.repository.CampaignRepository;
import org.example.repository.CouponRepository;
import org.example.repository.CouponUserRepository;
import org.example.service.CouponService;
import org.example.util.Messages;
import org.example.util.RequestStatus;
import org.example.util.ResponseCodes;
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

    @Autowired
    Messages messages;


    @Override
    public ApiResponse createCoupon(DTO dto) {
        processData(dto);
        ApiResponse response = new ApiResponse();
        response.setResponseCode(ResponseCodes.SUCCESS);
        response.setStatus(RequestStatus.SUCCESS.getStatusMessage());
        response.setMessage("Coupons saved succesfully");
        return response;
    }

//    @Transactional
//    public void saveCouponEntity(List<CouponEntity> couponEntityList) {
//        couponRepository.saveAll(couponEntityList);
//
//    }

    @Transactional
    public void processData(DTO dto) {
        if (validateDTO(dto)) {
            AppEntity appEntity = createAppEntity(dto);
            appRepository.save(appEntity);

            CampaignEntity campaignEntity = createCampaignEntity(dto, appEntity);
            campaignRepository.save(campaignEntity);

            dto.getLogic().parallelStream().forEach(data -> {
                System.out.println("Thread: " + Thread.currentThread().getName());
                if (validateCouponDTO(data)) {
                    Set<String> strings = validateCouponNumber(data);
                    List<CouponEntity> coupons = strings.parallelStream()
                            .map(couponNumber -> getCouponEntity(data, couponNumber, campaignEntity))
                            .collect(Collectors.toList());
                    couponRepository.saveAll(coupons);
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
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE,"Pattern Cannot be Empty");
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
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE,"Invalid Coupon Count");
        }

        validateDateRange(dto.getStartDate(), dto.getEndDate());

        return true;
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE,"Start date or End date cannot be empty");
        }

        if (startDate.isAfter(endDate)) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE,"Start Date should be before the end date");
        }
    }

    public boolean validateCouponDTO(CouponDTO couponDto) {

        List<String> values = Arrays.asList("rs", "point", "currency");

        if (!values.contains(couponDto.getType().toLowerCase()) || couponDto.getType().trim().isEmpty()) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE,"Type cannot contain values other than 'CURRENCY' and 'POINT'");

        }


        if (!values.contains(couponDto.getDisplayValue().toLowerCase()) || couponDto.getDisplayValue().trim().isEmpty()) {
            throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE,"Display Value cannot contain values other than 'RS' and 'Point'");
        }
        return true;
    }

    @Override
    public ApiResponse checkCoupon(String number) {
        CouponEntity couponEntity = couponRepository.getCouponEntityByNumber(number);
        ApiResponse apiResponse = new ApiResponse();

        // check the coupon can use or not
        if ((couponEntity != null) && (couponEntity.getUsageCount() == 0 || (couponEntity.getUsageCount() == 1 && couponEntity.getIsValid()) || couponEntity.getUsageCount() > 1)) {
            apiResponse.setResponseCode(ResponseCodes.SUCCESS);
            apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
            apiResponse.setMessage("Coupon Can Use");
        }else {
            apiResponse.setResponseCode(ResponseCodes.BAD_REQUEST_CODE);
            apiResponse.setStatusCode(RequestStatus.BAD_REQUEST.getStatusCode());
            apiResponse.setMessage("Coupon Can't Use");
        }
        return apiResponse;
    }

    @Transactional
    @Override
    public ApiResponse useCoupon(CouponUserDTO couponUserDTO, String number) {

        ApiResponse apiResponse = new ApiResponse();

        // change coupon status
        if (couponUserDTO.getUsedDate() != null && couponUserDTO.getUsedTime() != null && couponUserDTO.getUser() != null && Objects.equals(checkCoupon(number).getResponseCode(), ResponseCodes.SUCCESS)) {
            CouponUserEntity couponUserEntity = modelMapper.map(couponUserDTO, CouponUserEntity.class);
            Optional<UserEntity> userEntity = userRepository.findById(couponUserEntity.getUser().getUserId());
            CouponEntity couponEntity = couponRepository.getCouponEntityByNumber(number);

            if(userEntity.isPresent()) {
                // change isValid if usage count == 1
                if(couponEntity.getUsageCount() == 1){
                    couponRepository.updateCouponValidity(number);
                // change coupon usage
                }else if (couponEntity.getUsageCount() > 1){
                    changeCouponUsageCount(number);
                }
                couponUserRepository.save(couponUserEntity);
                apiResponse.setResponseCode(ResponseCodes.SUCCESS);
                apiResponse.setStatus(RequestStatus.SUCCESS.getStatusMessage());
                apiResponse.setMessage("Coupon Used Successfully");
            }else{
                apiResponse.setResponseCode(ResponseCodes.BAD_REQUEST_CODE);
                apiResponse.setStatus(RequestStatus.BAD_REQUEST.getStatusMessage());
                apiResponse.setMessage("Coupon Failed to Apply because of User Not Found");
            }
        }else{
            apiResponse.setResponseCode(ResponseCodes.BAD_REQUEST_CODE);
            apiResponse.setStatus(RequestStatus.BAD_REQUEST.getStatusMessage());
            apiResponse.setMessage("Coupon Failed to Apply");
        }
        return apiResponse;
    }
    @Override
    public void changeCouponUsageCount(String number) {
        CouponEntity couponEntity = couponRepository.getCouponEntityByNumber(number);
        int couponUsageCount = couponEntity.getUsageCount();
        couponUsageCount--;
        couponRepository.updateCouponUsageCount(couponUsageCount, number);
    }


    @Override
    public ApiResponse getCoupons(Pageable pageable) {
        Page<CouponEntity> all = couponRepository.findAll(pageable);
        ApiResponse response = new ApiResponse();
        response.setCouponList(all);
        response.setStatus(RequestStatus.SUCCESS.getStatusMessage());
        response.setResponseCode(ResponseCodes.SUCCESS);
        return response;

    }


}
