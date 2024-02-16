package org.example.service;

import org.example.dto.AdminDTO;
import org.example.dto.ApiResponse;

public interface AdminService {
    ApiResponse addAdmin(AdminDTO adminDTO);
}
