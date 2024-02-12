package org.example.controller;

import org.example.dto.ApiResponse;
import org.example.dto.DTO;
import org.example.service.CouponService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponControllerTest {

    @InjectMocks
    private CouponController couponController;

    @Mock
    private CouponService couponService;

    @Mock
    private DTO dto;





    @Test
    void createCoupon() {
        when(couponController.createCoupon(dto)).thenReturn(new ApiResponse());
        ApiResponse apiResponse = couponController.createCoupon(dto);

        assertNotNull(apiResponse);
    }

    @Test
    void getCoupons() {
        int pageSize = 10;
        int page= 0;
        when(couponController.getCoupons(page,pageSize)).thenReturn(new ApiResponse());
        ApiResponse apiResponse = couponController.getCoupons(page,pageSize);

        assertNotNull(apiResponse);
    }
}