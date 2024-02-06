package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.AppDTO;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "campaign")
public class CampaignEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int campaignId;
    private String campaignName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int couponCount;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private AppEntity appEntity;
}
