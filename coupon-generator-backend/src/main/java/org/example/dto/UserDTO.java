package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int userId;
    private String userName;
    private String email;
    private String password;

    public UserDTO(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
}
