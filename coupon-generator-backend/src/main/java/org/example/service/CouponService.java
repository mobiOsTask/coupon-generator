package org.example.service;

import org.example.dto.DTO;
import org.example.entity.CouponEntity;

import java.util.List;

public interface CouponService {
    void createCoupon(DTO dto);

    List<CouponEntity> getCoupons();
    boolean useCoupon(String number);
}
