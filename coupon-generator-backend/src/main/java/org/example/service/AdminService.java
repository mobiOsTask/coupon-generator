package org.example.service;

import org.example.dto.AdminDTO;
import org.example.dto.Responses.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    ApiResponse signUpAdmin(AdminDTO adminDTO);
    ApiResponse getAdmins(Pageable pageable);
    ApiResponse adminLogIn(String name, String password);
    boolean isExistByName(String name);
    boolean isExistByEmail(String address);
}