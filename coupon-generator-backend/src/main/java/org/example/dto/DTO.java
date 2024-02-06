package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTO {
    private int appId;
    private String campaignName;
    private String startDate;
    private String endDate;
    private int couponCount;
    private List<CouponDTO> logic;
}
