package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppDTO {
    private int appId;
    private String appName;
    private String webSiteURL;
    private String contactNumber;
}
