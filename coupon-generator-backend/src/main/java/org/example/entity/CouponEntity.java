package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon")
public class CouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int couponId;
    private int count;
    private String type;
    private String displayValue;
    private int length;
    private String regex;
    private boolean usageCount;
    private double amount;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private CampaignEntity campaignEntity;
}
