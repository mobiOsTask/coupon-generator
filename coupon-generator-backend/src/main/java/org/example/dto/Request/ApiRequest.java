package org.example.dto.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
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
    LocalDate dateFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate dateTo;
    double minAmount;
    double maxAmount;
    boolean isRedeemable;
    int campaign_id;
    private Integer page;
    private Integer pageCount;
    private String searchValue;

    public void setIsRedeemable(boolean isRedeemable){
        this.isRedeemable = isRedeemable;
    }

    public boolean getIsRedeemable(){
        return isRedeemable;
    }


}
