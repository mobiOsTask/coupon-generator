package org.example.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String userName;
    private String address;
    private String roleName;
    private String password;
    private int adminId;
}
