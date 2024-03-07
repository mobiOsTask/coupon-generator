package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private int adminId;
    private String userName;
    private String password;
    private String email;

    public AdminDTO(String name, String password, String address) {
        this.userName = name;
        this.password = password;
        this.email = address;
    }

}
