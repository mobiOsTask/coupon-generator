package org.example.service.impl;

import org.example.dto.AdminDTO;
import org.example.dto.ApiResponse;
import org.example.entity.AdminEntity;
import org.example.repository.AdminRepository;
import org.example.service.AdminService;
import org.example.util.Messages;
import org.example.util.RequestStatus;
import org.example.util.ResponseCodes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    Messages messages;

    @Override
    public ApiResponse addAdmin(AdminDTO adminDTO) {
        AdminEntity adminEntity = modelMapper.map(adminDTO, AdminEntity.class);
        adminRepository.save(adminEntity);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.ADMIN_CREATED, null));
        return apiResponse;
    }
}
