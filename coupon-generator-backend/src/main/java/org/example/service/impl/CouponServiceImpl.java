package org.example.service.impl;

import org.example.repository.CouponRepository;
import org.example.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    CouponRepository couponRepository;
}
