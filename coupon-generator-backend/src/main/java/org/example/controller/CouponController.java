package org.example.controller;

import jakarta.validation.Valid;
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
    public ApiResponse createCoupon(@Valid @RequestBody DTO dto){
        return service.createCoupon(dto);

    }

    @GetMapping("/")
    public ApiResponse getCoupons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return service.getCoupons(pageable);
    }

    @GetMapping("/check-coupon")
    public ApiResponse checkCoupon(@RequestParam("number") String number){
        return service.checkCoupon(number);
    }

    @GetMapping("/use-coupon")
    public ApiResponse useCoupon(@RequestBody CouponUserDTO couponUserDTO, @RequestParam("number") String number){
        return service.useCoupon(couponUserDTO, number);
    }
}
