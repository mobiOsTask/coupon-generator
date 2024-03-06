package org.example.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    // User Sign Up
    private String userName;
    private String email;
    private String roleName;
    private String password;
    private int adminId;

    // Admin Sign Up
    private String adminName;
    private String adminPassword;
    private String adminEmail;

}
