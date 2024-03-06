package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private int adminId;
    private String name;
    private String password;
    private String address;

    public AdminDTO(String name, String password, String address) {
        this.name = name;
        this.password = password;
        this.address = address;
    }
}
