package org.example.service.impl;

import jakarta.transaction.Transactional;
import org.example.dto.ApiResponse;
import org.example.dto.AppDTO;
import org.example.entity.AppEntity;
import org.example.entity.CouponEntity;
import org.example.repository.AppRepository;
import org.example.repository.CouponRepository;
import org.example.service.AppService;
import org.example.util.RequestStatus;
import org.example.util.ResponseCodes;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    AppRepository appRepository;

    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);



    @Override
    public ApiResponse createApp(AppDTO appDTO) {
        logger.info("Create App Starts");
        AppEntity appEntity = modelMapper.map(appDTO, AppEntity.class);
        appRepository.save(appEntity);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage("App Created Successfully");
        logger.info("App Created {} ", appEntity.getAppId());
        return apiResponse;
    }

    @Override
    public ApiResponse deleteApp(int appId) {
        logger.info("Delete App Starts");
        appRepository.deleteById(appId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage("App Deleted Successfully");
        logger.info("App Deleted {} ", appId);
        return apiResponse;
    }

    @Override
    public ApiResponse getAllApps() {
        logger.info("Get Apps Starts");
        List<AppEntity> appEntities = appRepository.findAll();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setAppList(appEntities);
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        logger.info("Get Apps {} ", appEntities);
        return apiResponse;
    }

    @Override
    public ApiResponse updateApp(AppDTO appDTO, int appId) {
        logger.info("Update App Starts");
        AppEntity appEntity = appRepository.findById(appId).get();

        appEntity.setAppName(appDTO.getAppName());
        appEntity.setWebSiteURL(appDTO.getWebSiteURL());
        appEntity.setContactNumber(appDTO.getContactNumber());

        appRepository.save(appEntity);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage("App Updated Successfully");
        logger.info("App Updated {} ", appEntity.getAppId());
        return apiResponse;
    }
}
