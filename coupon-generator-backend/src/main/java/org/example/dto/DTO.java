package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTO {
    private int appId;
    private String campaignName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int couponCount;
    private List<CouponDTO> logic;
}
