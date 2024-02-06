package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponDTO {
    private int couponId;
    private int count;
    private String type;
    private String displayValue;
    private int length;
    private String regex;
    private boolean usageCount;
    private double amount;
}
