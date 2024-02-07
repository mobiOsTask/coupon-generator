package org.example.controller;

import org.example.dto.DTO;
import org.example.entity.CouponEntity;
import org.example.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/coupon")
public class CouponController {
    @Autowired
    CouponService service;


    @PostMapping
    public void createCoupon(@RequestBody DTO dto){
        service.createCoupon(dto);
    }

    @GetMapping
    public List<CouponEntity> getCoupons(){
        return service.getCoupons();
    }

    @GetMapping("/use-coupon")
    public ResponseEntity<String> useCoupon(@RequestParam("number") String number){
        boolean isUsed = service.useCoupon(number);
        return new ResponseEntity<>(isUsed ? "Coupon Can Use" : "Coupon Can't Use", HttpStatus.OK);
    }
}
