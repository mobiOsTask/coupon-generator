package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiRequest {

    //filtering conditions
    String type;
    String displayValue;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime dateFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime dateTo;
    double minAmount;
    double maxAmount;
    boolean isRedeemable;
    int campaign_id;
    private Integer page;
    private Integer pageCount;
    private String searchValue;

    String refNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date date;
    String note;
    String isActive;



    String status;
    String description;
    Long id;
    Long productId;
    Long technicianId;

    List<Long> idList;

    //driver
    private String name;
    private String userName;
    private String password;
    private String mobileNo;
    private String email;
    private String area;
    private String address;
    private String nic;


    private Long ruleId;
    private Integer percentage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;



    private String userType;
    private Long userId;
    private Long deliveryDataFileId;


    private Boolean sendSMS;
    private String content;


}
