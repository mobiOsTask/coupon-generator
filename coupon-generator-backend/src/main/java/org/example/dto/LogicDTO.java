package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogicDTO {
    private int couponId;
    private int count;
    private String type;
    private String displayValue;
    private int length;
    private String regex;
    private int usageCount;
    private double amount;
    private boolean isRedeemable;
    private LocalDate startDate;
    private LocalDate endDate;


    public void setIsValid(boolean isValid){
        this.isRedeemable = isValid;
    }

    public boolean getIsValid(){
        return isRedeemable;
    }
}
