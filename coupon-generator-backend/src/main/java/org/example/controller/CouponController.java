package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.CouponUserDTO;
import org.example.dto.DTO;
import org.example.entity.CouponEntity;
import org.example.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/coupon")
public class CouponController {
    @Autowired
    CouponService service;


    @PostMapping("save-coupons")
    public Map<String,String> createCoupon(@Valid @RequestBody DTO dto){
        Map<String,String> response = new HashMap<>();
        service.createCoupon(dto);
        response.put("status","Coupons created successfully");
        return response;
    }

    @GetMapping("get-coupons")
    public Page<CouponEntity> getCoupons(
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
