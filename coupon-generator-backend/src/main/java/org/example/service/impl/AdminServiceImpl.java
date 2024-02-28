package org.example.service.impl;

import jakarta.transaction.Transactional;
import org.example.dto.AdminDTO;
import org.example.dto.Responses.ApiResponse;
import org.example.entity.AdminEntity;
import org.example.repository.AdminRepository;
import org.example.service.AdminService;
import org.example.util.Messages;
import org.example.util.RequestStatus;
import org.example.util.ResponseCodes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    Messages messages;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public ApiResponse addAdmin(AdminDTO adminDTO) {
        AdminEntity adminEntity = modelMapper.map(adminDTO, AdminEntity.class);

        String encryptedPassword = bCryptPasswordEncoder.encode(adminDTO.getPassword());
        adminEntity.setPassword(encryptedPassword);
        adminRepository.save(adminEntity);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.ADMIN_CREATED, null));
        return apiResponse;
    }

    @Override
    public ApiResponse getAdmins(Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        Page<AdminEntity> adminEntityPage = adminRepository.findAll(pageable);
        if(!adminEntityPage.isEmpty()){
            apiResponse.setAdminList(adminEntityPage);
            apiResponse.setStatus(RequestStatus.SUCCESS.getStatusMessage());
            apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        }else{
            apiResponse.setStatus(RequestStatus.NOT_FOUND.getStatusMessage());
            apiResponse.setResponseCode(ResponseCodes.NOT_FOUND);
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.ADMINS_ARE_EMPTY, null));
        }
        return apiResponse;
    }

    @Transactional
    @Override
    public ApiResponse adminLogIn(String name, String password) {
        ApiResponse apiResponse = new ApiResponse();
        List<AdminEntity> adminEntities = adminRepository.adminLogIn(name);
        if(!adminEntities.isEmpty()){
            for (AdminEntity adminEntity: adminEntities) {
                if(bCryptPasswordEncoder.matches(password, adminEntity.getPassword())){
                    adminRepository.updateLogIn(adminEntity.getAdminId());
                    apiResponse.setStatus(RequestStatus.SUCCESS.getStatusMessage());
                    apiResponse.setResponseCode(ResponseCodes.SUCCESS);
                    apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.ADMIN_LOG_IN, null));
                }
            }
        }else{
            apiResponse.setStatus(RequestStatus.NOT_FOUND.getStatusMessage());
            apiResponse.setResponseCode(ResponseCodes.NOT_FOUND);
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.ADMIN_LOG_IN_FAILED, null));
        }
        return apiResponse;
    }
}
