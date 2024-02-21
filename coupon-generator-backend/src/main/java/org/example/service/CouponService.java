package org.example.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.ApiRequest;
import org.example.dto.ApiResponse;
import org.example.dto.CouponUserDTO;
import org.example.dto.DTO;
import org.example.entity.CouponEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface CouponService {
    void createCoupon(DTO dto);

    ApiResponse getCoupons(ApiRequest apiRequest);

    ApiResponse checkCoupon(String number);
    ApiResponse useCoupon(HttpServletRequest request, CouponUserDTO couponUserDTO, String number);
    void changeCouponUsageCount(String number);
    boolean isValidDate(CouponEntity couponEntity);
    ApiResponse getAppByCouponNumber(String number);
    ApiResponse getCampaignByCouponNumber(String number);
    ApiResponse getCouponEntityByCampaignId(int campaignId, Pageable pageable);
    ApiResponse getCampaignEntityByAppId(int appId, Pageable pageable);
    ApiResponse getRedeemableCouponsByCampaignId(int campaignId, Pageable pageable);
    ApiResponse getRedeemableCampaignsByAppId(int appId, Pageable pageable);
    ApiResponse getRedeemableCoupons(PageRequest pageRequest,boolean is_redeemable);

    ApiResponse getCouponData(PageRequest pageRequest, String couponNumber);
}
