package org.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.entity.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;


@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse extends  Response{

    Page<CouponEntity> couponList;
    List<CampaignEntity> campaignList;
    List<AppEntity> appList;
    List<UserEntity> userList;

    CouponEntity couponEntity;

}
