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
    Page<CouponUserEntity> couponUserList;
    Page<CampaignEntity> campaignList;
    Page<AdminEntity> adminList;
    CampaignEntity campaignEntity;
    List<AppEntity> appList;
    AppEntity appEntity;
    List<UserEntity> userList;
    UserEntity userEntity;
    CouponEntity couponEntity;

}
