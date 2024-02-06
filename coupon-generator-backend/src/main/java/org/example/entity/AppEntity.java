package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app")
public class AppEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int appId;
    private String appName;
    private String webSiteURL;
    private String contactNumber;
}
