package org.example.service;

import org.example.dto.ApiResponse;
import org.example.dto.CouponUserDTO;
import org.example.dto.DTO;
import org.springframework.data.domain.Pageable;

public interface CouponService {
    ApiResponse createCoupon(DTO dto);

    ApiResponse getCoupons(Pageable pageable);

    boolean checkCoupon(String number);
    boolean useCoupon(CouponUserDTO couponUserDTO, String number);
    int changeCouponUsageCount(String number);
}
