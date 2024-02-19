package org.example.service;

import org.example.dto.AdminDTO;
import org.example.dto.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    ApiResponse addAdmin(AdminDTO adminDTO);
    ApiResponse getAdmins(Pageable pageable);
    ApiResponse adminLogIn(String name, String password);
}
