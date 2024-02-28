package org.example.controller;

import org.example.dto.DTO;
import org.example.service.CouponService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CouponControllerTest {

    @InjectMocks
    private CouponController couponController;

    @Mock
    private CouponService couponService;

    @Mock
    private DTO dto;







//    @Test
//    void getCoupons() {
//        int pageSize = 10;
//        int page= 0;
//        when(couponController.getCoupons(page,pageSize)).thenReturn(new ApiResponse());
//        ApiResponse apiResponse = couponController.getCoupons(page,pageSize);
//
//        assertNotNull(apiResponse);
//    }
}