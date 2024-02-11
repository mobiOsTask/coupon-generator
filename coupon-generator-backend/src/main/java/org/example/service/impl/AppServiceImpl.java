package org.example.service.impl;

import org.example.dto.ApiResponse;
import org.example.dto.AppDTO;
import org.example.entity.AppEntity;
import org.example.repository.AppRepository;
import org.example.service.AppService;
import org.example.util.RequestStatus;
import org.example.util.ResponseCodes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    AppRepository appRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ApiResponse createApp(AppDTO appDTO) {
        AppEntity appEntity = modelMapper.map(appDTO, AppEntity.class);
        appRepository.save(appEntity);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage("App Created Successfully");
        return apiResponse;
    }

    @Override
    public ApiResponse deleteApp(int appId) {
        appRepository.deleteById(appId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage("App Deleted Successfully");
        return apiResponse;
    }

    @Override
    public ApiResponse getAllApps() {
        List<AppEntity> appEntities = appRepository.findAll();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setAppList(appEntities);
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        return apiResponse;
    }

    @Override
    public ApiResponse updateApp(AppDTO appDTO, int appId) {
        AppEntity appEntity = appRepository.findById(appId).get();

        appEntity.setAppName(appDTO.getAppName());
        appEntity.setWebSiteURL(appDTO.getWebSiteURL());
        appEntity.setContactNumber(appDTO.getContactNumber());

        appRepository.save(appEntity);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage("App Updated Successfully");
        return apiResponse;
    }
}
