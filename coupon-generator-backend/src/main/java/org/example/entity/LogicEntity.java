package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "logic")
public class LogicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logicId;
    private int count;
    private String type;
    private String displayValue;
    @JsonIgnore
    private int length;
    @JsonIgnore
    private String regex;
    private int usageCount;
    private double amount;
    private LocalDate startDate;
    private LocalDate endDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private CampaignEntity campaignEntity;
}
