package org.example.service;

import org.example.dto.ApiResponse;
import org.example.dto.RolesDTO;

public interface RoleService {
    ApiResponse addRole(RolesDTO rolesDTO);
}
