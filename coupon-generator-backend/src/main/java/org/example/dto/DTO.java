package org.example.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Invalid campaign Name")
    private String campaignName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int couponCount;
    private List<LogicDTO> logic;
}
