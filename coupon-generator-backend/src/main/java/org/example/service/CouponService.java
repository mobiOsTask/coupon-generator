package org.example.service;

import org.example.dto.ApiResponse;
import org.example.dto.CouponUserDTO;
import org.example.dto.DTO;
import org.springframework.data.domain.Pageable;

public interface CouponService {
    void createCoupon(DTO dto);

    ApiResponse getCoupons(Pageable pageable);

    ApiResponse checkCoupon(String number);
    ApiResponse useCoupon(CouponUserDTO couponUserDTO, String number);
    void changeCouponUsageCount(String number);
}
