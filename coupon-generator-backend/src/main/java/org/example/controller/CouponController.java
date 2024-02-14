package org.example.controller;

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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


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
    public void createCoupon(@Valid @RequestBody DTO dto){
        service.createCoupon(dto);
    }

    @PostMapping("/manage")
    public ApiResponse getCoupons(@RequestBody ApiRequest apiRequest) {

        System.out.println(apiRequest);
        return service.getCoupons(apiRequest);
    }

    @GetMapping("/check-coupon")
    public ApiResponse checkCoupon(@RequestParam("number") String number){
        return service.checkCoupon(number);
    }

    @PostMapping("/use-coupon")
    public ApiResponse useCoupon(@RequestBody CouponUserDTO couponUserDTO, @RequestParam("number") String number){
        return service.useCoupon(couponUserDTO, number);
    }
}
