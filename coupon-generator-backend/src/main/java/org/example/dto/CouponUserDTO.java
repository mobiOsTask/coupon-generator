package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponUserDTO {
    private int usedCouponId;
    private LocalDate usedDate;
    private LocalTime usedTime;
    private UserDTO user;
}
