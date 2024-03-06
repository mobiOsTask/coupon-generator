package org.example.service;

import org.example.dto.Responses.ApiResponse;
import org.example.dto.RolesDTO;

public interface RoleService {
    ApiResponse addRole(RolesDTO rolesDTO);

    ApiResponse getRoles();
}
