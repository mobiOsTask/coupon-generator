package org.example.service;

import org.example.dto.CouponUserDTO;
import org.example.dto.DTO;
import org.example.entity.CouponEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponService {
    void createCoupon(DTO dto);

    Page<CouponEntity> getCoupons(Pageable pageable);

    boolean checkCoupon(String number);
    boolean useCoupon(CouponUserDTO couponUserDTO, String number);
}
