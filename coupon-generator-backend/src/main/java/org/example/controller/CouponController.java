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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @PostMapping("/save-coupons")
    public ApiResponse createCoupon(@Valid @RequestBody DTO dto){
        return service.createCoupon(dto);

    }

    @GetMapping("/get-coupons")
    public ApiResponse getCoupons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return service.getCoupons(pageable);
    }

    @GetMapping("/check-coupon")
    public ResponseEntity<String> checkCoupon(@RequestParam("number") String number){
        boolean canUse = service.checkCoupon(number);
        return new ResponseEntity<>(canUse ? "Coupon Can Use" : "Coupon Can't Use", HttpStatus.OK);
    }

    @GetMapping("/use-coupon")
    public ResponseEntity<String> useCoupon(@RequestBody CouponUserDTO couponUserDTO, @RequestParam("number") String number){
        boolean isUsed = service.useCoupon(couponUserDTO, number);
        return new ResponseEntity<>(isUsed ? "Coupon Used" : "Coupon Use Failed", HttpStatus.OK);
    }
}
