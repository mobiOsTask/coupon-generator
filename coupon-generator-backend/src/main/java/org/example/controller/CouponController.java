package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.DTO;
import org.example.entity.CouponEntity;
import org.example.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public void createCoupon(@Valid @RequestBody DTO dto){
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
