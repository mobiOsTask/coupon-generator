package org.example.service.impl;

import org.example.dto.Responses.ApiResponse;
import org.example.dto.RolesDTO;
import org.example.entity.ERole;
import org.example.entity.RolesEntity;
import org.example.repository.RoleRepository;
import org.example.service.RoleService;
import org.example.util.RequestStatus;
import org.example.util.ResponseCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public ApiResponse addRole(RolesDTO rolesDTO) {
        ApiResponse apiResponse = new ApiResponse();
        RolesEntity rolesEntity = new RolesEntity();

        rolesEntity.setName(ERole.valueOf(rolesDTO.getRoleName()));

        try{
            roleRepository.save(rolesEntity);
            apiResponse.setMessage("Role added successfully");
            apiResponse.setStatus(RequestStatus.SUCCESS.getStatusMessage());
            apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        } catch (Exception e){
            apiResponse.setMessage("Error adding Role");
            apiResponse.setStatus(RequestStatus.BAD_REQUEST.getStatusMessage());
            apiResponse.setResponseCode(ResponseCodes.BAD_REQUEST_CODE);
        }

        return apiResponse;
    }

    @Override
    public ApiResponse getRoles() {
        List<RolesEntity> rolesEntityList = roleRepository.findAll();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setRoleList(rolesEntityList);
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatus(RequestStatus.SUCCESS.getStatusMessage());

        return apiResponse;
    }
}
