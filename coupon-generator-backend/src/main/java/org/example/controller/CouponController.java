package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.example.dto.ApiRequest;
import org.example.dto.ApiResponse;
import org.example.dto.CouponUserDTO;
import org.example.dto.DTO;
import org.example.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;



@RestController
@CrossOrigin
@RequestMapping("/coupon")
public class CouponController {

    public static String TIME_ZONE;

    @Value("Asia/Colombo")
    public void setNameStatic(String name) {
        CouponController.TIME_ZONE = name;
    }

    @Autowired
    CouponService service;


    @PostMapping("/")
    public ApiResponse createCoupon(@Valid @RequestBody DTO dto) {
        return service.createCoupon(dto);
    }

    @PostMapping("/manage")
    public ApiResponse getCoupons(@RequestBody ApiRequest apiRequest) {
        return service.getCoupons(apiRequest);
    }

    @PostMapping("/coupon-data-count")
    public ApiResponse getCouponCounts(@RequestBody ApiRequest apiRequest) {
        return service.getCouponCounts(apiRequest);
    }

    @GetMapping("/redeemable")
    public ApiResponse getRedeemableCoupons(@RequestParam ("page") int page,
                                            @RequestParam("pageSize") int pageSize,
                                            @RequestParam("redeemable") boolean is_redeemable ){
        PageRequest pageRequest = PageRequest.of(page,pageSize);
        return service.getRedeemableCoupons(pageRequest,is_redeemable);
    }

    @GetMapping("/coupon-data")
    public ApiResponse getAllCouponData(@RequestParam ("number") String couponNumber, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
        PageRequest pageRequest = PageRequest.of(page,pageSize);
        return service.getCouponData(pageRequest,couponNumber);
    }

    @GetMapping("/check-coupon")
    public ApiResponse checkCoupon(@RequestParam("number") String number) {
        return service.checkCoupon(number);
    }

    @PostMapping("/use-coupon")
    public ApiResponse useCoupon(HttpServletRequest request, @RequestBody CouponUserDTO couponUserDTO, @RequestParam("number") String number) {
        return service.useCoupon(request, couponUserDTO, number);
    }

    @GetMapping("/campaign/coupons")
    public ApiResponse getCouponsByCampaignId(@RequestParam("campaignId") int campaignId, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return service.getCouponEntityByCampaignId(campaignId, pageable);
    }

    @GetMapping("/app/campaigns")
    public ApiResponse getCampaignsByAppId(@RequestParam("appId") int appId, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return service.getCampaignEntityByAppId(appId, pageable);
    }

    @GetMapping("/campaign/redeemable-coupons")
    public ApiResponse getRedeemableCouponsByCampaignId(@RequestParam("campaignId") int campaignId, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return service.getRedeemableCouponsByCampaignId(campaignId, pageable);
    }

    @GetMapping("/app/valid-campaigns")
    public ApiResponse getValidCampaignsByAppId(@RequestParam("appId") int appId, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return service.getRedeemableCampaignsByAppId(appId, pageable);
    }
}
