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


import java.time.LocalDate;
import java.time.LocalDateTime;
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
    LogicRepository logicRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CampaignRepository campaignRepository;

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
                if (Utils.validateCouponDTO(data,dto.getStartDate(),dto.getEndDate())) { // validate coupon dto's type and display value
                    LogicEntity logicEntity = Utils.getLogicEntity(data,campaignEntity);
                    logicRepository.save(logicEntity);
                    Set<String> couponNumbers = Utils.validateCouponNumber(data);
                    List<CouponEntity> coupons = couponNumbers.stream()
                            .map(couponNumber -> Utils.getCouponEntity(couponNumber, logicEntity, data.getIsRedeemable()))
                            .collect(Collectors.toList());
//                    System.out.println(coupons);
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
        if ((couponEntity != null) && (couponEntity.getLogicEntity().getUsageCount() == 0 || (couponEntity.getLogicEntity().getUsageCount() == 1 && couponEntity.getIsRedeemable()) || couponEntity.getLogicEntity().getUsageCount() > 1) && isValidDate(couponEntity)) {
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
                if (couponEntity.getLogicEntity().getUsageCount() == 1) {
                    couponRepository.updateCouponValidity(number);
                    // change coupon usage
                } else if (couponEntity.getLogicEntity().getUsageCount() > 1) {
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
        int couponUsageCount = couponEntity.getLogicEntity().getUsageCount();
        couponUsageCount--;
        couponRepository.updateCouponUsageCount(couponUsageCount, number);
        logger.info("Coupon usage count ends");
    }

    @Override
    public boolean isValidDate(CouponEntity couponEntity) {
        LocalDate currentDate = LocalDate.now();
        return couponEntity.getLogicEntity().getStartDate().isBefore(currentDate) && couponEntity.getLogicEntity().getEndDate().isAfter(currentDate);
    }


    @Override
    public ApiResponse getCoupons(ApiRequest apiRequest) {

        PageRequest pageRequest = PageRequest.of(apiRequest.getPage(), apiRequest.getPageCount());
        double minAmount = apiRequest.getMinAmount();
        double maxAmount = apiRequest.getMaxAmount();
        LocalDateTime dateFrom = apiRequest.getDateFrom();
        LocalDateTime dateTo = apiRequest.getDateTo();
        String type = apiRequest.getType();
        String displayValue = apiRequest.getDisplayValue();


        if (dateFrom != null) {
//            String date = Utils.formatDateOnly(dateFrom);
//            System.out.println(date);
//            dateFrom = Utils.formatDateAndTime(date + " 00:00:00");
//            System.out.println(dateFrom);
        }
        if (dateTo != null) {
//            String date = Utils.formatDateOnly(dateTo);
//            System.out.println(date);
//            dateTo = Utils.parseDateAndTime(date + " 23:59:59");
//            System.out.println(dateTo);
        }

        String isSearch = null;
        if (null != apiRequest.getSearchValue()) {
            isSearch = "true";
        }
        Page<CouponEntity> all = couponRepository.getAll(apiRequest.getSearchValue(), isSearch, dateFrom, dateTo, minAmount, maxAmount, type, displayValue, pageRequest);

        ApiResponse response = new ApiResponse();
        response.setCouponList(all);
        response.setStatus(RequestStatus.SUCCESS.getStatusMessage());
        response.setResponseCode(ResponseCodes.SUCCESS);
        return response;
    }
    @Override
    public ApiResponse getAppByCouponNumber(String number){
        ApiResponse apiResponse = new ApiResponse();

        int appId = couponRepository.getAppByCouponNumber(number);
        apiResponse.setMessage("App Id : "+appId);
        apiResponse.setStatus(RequestStatus.SUCCESS.getStatusMessage());
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);

        return apiResponse;
    }
    @Override
    public ApiResponse getCampaignByCouponNumber(String number){
        ApiResponse apiResponse = new ApiResponse();
        int campaignId = couponRepository.getCampaignByCouponNumber(number);
        apiResponse.setMessage("Campaign Id : "+campaignId);
        apiResponse.setStatus(RequestStatus.SUCCESS.getStatusMessage());
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);

        return apiResponse;
    }
    @Override
    public ApiResponse getCouponEntityByCampaignId(int campaignId, Pageable pageable){
        ApiResponse apiResponse = new ApiResponse();
        Page<CouponEntity> couponEntityList = couponRepository.getCouponEntitiesByCampaignId(campaignId, pageable);
        return getCouponApiResponse(apiResponse, couponEntityList);
    }

    private ApiResponse getCouponApiResponse(ApiResponse apiResponse, Page<CouponEntity> couponEntityList) {
        if(couponEntityList != null){
            apiResponse.setCouponList(couponEntityList);
            apiResponse.setStatus(RequestStatus.SUCCESS.getStatusMessage());
            apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        }else{
            apiResponse.setStatus(RequestStatus.NOT_FOUND.getStatusMessage());
            apiResponse.setResponseCode(ResponseCodes.NOT_FOUND);
        }
        return apiResponse;
    }

    @Override
    public ApiResponse getCampaignEntityByAppId(int appId, Pageable pageable){
        ApiResponse apiResponse = new ApiResponse();
        Page<CampaignEntity> campaignEntities = campaignRepository.getCampaignEntitiesByAppId(appId, pageable);
        return getCampaignApiResponse(apiResponse, campaignEntities);
    }

    @Override
    public ApiResponse getRedeemableCouponsByCampaignId(int campaignId, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        Page<CouponEntity> redeemableCouponEntities = couponRepository.getRedeemableCouponEntitiesByCampaignId(campaignId, pageable);
        return getCouponApiResponse(apiResponse, redeemableCouponEntities);
    }

    @Override
    public ApiResponse getRedeemableCampaignsByAppId(int appId, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        Page<CampaignEntity> campaignEntities = campaignRepository.getValidCampaignEntitiesByAppId(appId, pageable);
        return getCampaignApiResponse(apiResponse, campaignEntities);
    }

    private ApiResponse getCampaignApiResponse(ApiResponse apiResponse, Page<CampaignEntity> campaignEntities) {
        if(campaignEntities != null){
            apiResponse.setCampaignList(campaignEntities);
            apiResponse.setStatus(RequestStatus.SUCCESS.getStatusMessage());
            apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        }else{
            apiResponse.setStatus(RequestStatus.NOT_FOUND.getStatusMessage());
            apiResponse.setResponseCode(ResponseCodes.NOT_FOUND);
        }
        return apiResponse;
    }

}
