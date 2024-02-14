package org.example.service.impl;

import jakarta.transaction.Transactional;
import org.example.dto.ApiRequest;
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
import org.example.service.CouponService;
import org.example.service.CouponStorageService;
import org.example.util.Messages;
import org.example.util.RequestStatus;
import org.example.util.ResponseCodes;
import org.example.util.Utils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    CouponRepository couponRepository;


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
    CouponStorageService couponStorageService;

    private static final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Override
    public void createCoupon(DTO dto) {
        logger.info("Create coupon starts");
        processData(dto);
        logger.info("Create coupon ends");
    }

    public void processData(DTO dto) {
        logger.info("Process data starts");
        List<List<CouponEntity>> all = new ArrayList<>();
        if (Utils.validateDTO(dto)) { // validates coupon count, start date and end date

            AppEntity appEntity = appRepository.findByAppId(dto.getAppId());
            if (appEntity == null) {
                throw new DLAppValidationsException(ResponseCodes.BAD_REQUEST_CODE, "Invalid App ID"); // validate App Id
            }

            CampaignEntity campaignEntity = Utils.createCampaignEntity(dto, appEntity);
            couponStorageService.saveCampaign(campaignEntity); // saves campaign entity

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
            couponStorageService.saveCoupons(data);
        }
        logger.info("Process data ends");
    }


    @Override
    public ApiResponse checkCoupon(String number) {
        logger.info("Check coupon starts {} ", number);
        CouponEntity couponEntity = couponRepository.getCouponEntityByNumber(number);
        ApiResponse apiResponse = new ApiResponse();

        // check the coupon can use or not
        if ((couponEntity != null) && (couponEntity.getUsageCount() == 0 || (couponEntity.getUsageCount() == 1 && couponEntity.getIsValid()) || couponEntity.getUsageCount() > 1)) {
            apiResponse.setResponseCode(ResponseCodes.SUCCESS);
            apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.CAN_USE, null));
        } else {
            apiResponse.setResponseCode(ResponseCodes.BAD_REQUEST_CODE);
            apiResponse.setStatusCode(RequestStatus.BAD_REQUEST.getStatusCode());
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.CANNOT_USE, null));
        }
        logger.info("Check coupon ends");
        return apiResponse;
    }

    @Transactional
    @Override
    public ApiResponse useCoupon(CouponUserDTO couponUserDTO, String number) {
        logger.info("Coupon use starts {} ", couponUserDTO.getUsedCouponId());
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
                apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.USE_SUCCESS, null));
            } else {
                apiResponse.setResponseCode(ResponseCodes.BAD_REQUEST_CODE);
                apiResponse.setStatus(RequestStatus.BAD_REQUEST.getStatusMessage());
                apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.USE_FAILED_USER_NOT_FOUND, null));
            }
        } else {
            apiResponse.setResponseCode(ResponseCodes.BAD_REQUEST_CODE);
            apiResponse.setStatus(RequestStatus.BAD_REQUEST.getStatusMessage());
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.USE_FAILED, null));
        }
        logger.info("Coupon use ends");
        return apiResponse;
    }

    @Override
    public void changeCouponUsageCount(String number) {
        logger.info("Change coupon usage count starts");
        CouponEntity couponEntity = couponRepository.getCouponEntityByNumber(number);
        int couponUsageCount = couponEntity.getUsageCount();
        couponUsageCount--;
        couponRepository.updateCouponUsageCount(couponUsageCount, number);
        logger.info("Coupon usage count ends");
    }


    @Override
    public ApiResponse getCoupons(ApiRequest apiRequest) {

        PageRequest pageRequest = PageRequest.of(apiRequest.getPage(), apiRequest.getPageCount());
        double minAmount = apiRequest.getMinAmount();
        double maxAmount = apiRequest.getMaxAmount();
        Date dateFrom = apiRequest.getDateFrom();
        Date dateTo = apiRequest.getDateTo();
        String type = apiRequest.getType();;
        String displayValue = apiRequest.getDisplayValue();

        if (dateFrom != null) {
            String date = Utils.formatDateOnly(dateFrom);
            dateFrom = Utils.formatDateAndTime(date + " 00:00:00");
            System.out.println(dateFrom);
        }
        if (dateTo != null) {
            String date = Utils.formatDateOnly(dateTo);
            dateTo = Utils.formatDateAndTime(date + " 23:59:59");
            System.out.println(dateTo);
        }

        String isSearch = null;
        if (null != apiRequest.getSearchValue()) {
            isSearch = "true";
        }
        System.out.println(dateFrom);
        System.out.println(dateTo);
        Page<CouponEntity> all = couponRepository.getAll(apiRequest.getSearchValue(), isSearch, dateFrom, dateTo,pageRequest);

        ApiResponse response = new ApiResponse();
        response.setCouponList(all);
        response.setStatus(RequestStatus.SUCCESS.getStatusMessage());
        response.setResponseCode(ResponseCodes.SUCCESS);
        return response;
    }



}
