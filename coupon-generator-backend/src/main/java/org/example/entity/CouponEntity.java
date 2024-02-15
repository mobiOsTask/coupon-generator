package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon")
public class CouponEntity extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int couponId;
    private String number;
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isRedeemable;

    public void setIsRedeemable(boolean isValid){
        this.isRedeemable = isValid;
    }
    public boolean getIsRedeemable(){
        return isRedeemable;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "logic_id")
    private LogicEntity logicEntity ;
}
