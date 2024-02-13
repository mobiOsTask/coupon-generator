package org.example.service.impl;

import jakarta.transaction.Transactional;
import org.example.dto.ApiResponse;
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
import org.example.service.AppService;
import org.example.service.CouponService;
import org.example.util.Messages;
import org.example.util.RequestStatus;
import org.example.util.ResponseCodes;
import org.example.util.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


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

    @Autowired
    NativeCouponRepository nativeCouponRepository;


    @Override
    public void createCoupon(DTO dto) {
        processData(dto);
    }


    @Transactional
    public void processData(DTO dto) {
        List<List<CouponEntity>> all = new ArrayList<>();
        if (Utils.validateDTO(dto)) { // validates coupon count, start date and end date

            AppEntity appEntity = appRepository.findByAppId(dto.getAppId());
            if (appEntity == null) {
                throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE, "Invalid App ID"); // validate App Id
            }

            CampaignEntity campaignEntity = Utils.createCampaignEntity(dto, appEntity);
            campaignRepository.save(campaignEntity); // saves campaign entity

            dto.getLogic().parallelStream().forEach(data -> { //creates a parallel stream to implement parallel processing ,iterate through the dto's logic array
                System.out.println("Thread: " + Thread.currentThread().getName()); // prints the currently using thread
                if (Utils.validateCouponDTO(data)) { // validate coupon dto's type and display value
                    Set<String> couponNumbers = Utils.validateCouponNumber(data);  // create unique coupon numbers and validate them
                    List<CouponEntity> coupons = couponNumbers.stream()
                            .map(couponNumber -> Utils.getCouponEntity(data, couponNumber, campaignEntity))
                            .collect(Collectors.toList());
                    all.add(coupons);

                }

            });
        }

        for (List<CouponEntity> data : all) {
            System.out.println(Thread.currentThread().getName());
            nativeCouponRepository.batchSaveCoupons(data);
        }
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
        } else {
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

            if (userEntity.isPresent()) {
                // change isValid if usage count == 1
                if (couponEntity.getUsageCount() == 1) {
                    couponRepository.updateCouponValidity(number);
                    // change coupon usage
                } else if (couponEntity.getUsageCount() > 1) {
                    changeCouponUsageCount(number);
                }
                couponUserRepository.save(couponUserEntity);
                apiResponse.setResponseCode(ResponseCodes.SUCCESS);
                apiResponse.setStatus(RequestStatus.SUCCESS.getStatusMessage());
                apiResponse.setMessage("Coupon Used Successfully");
            } else {
                apiResponse.setResponseCode(ResponseCodes.BAD_REQUEST_CODE);
                apiResponse.setStatus(RequestStatus.BAD_REQUEST.getStatusMessage());
                apiResponse.setMessage("Coupon Failed to Apply because of User Not Found");
            }
        } else {
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
