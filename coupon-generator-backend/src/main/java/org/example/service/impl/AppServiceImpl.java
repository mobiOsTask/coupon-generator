package org.example.service.impl;

import jakarta.transaction.Transactional;
import org.example.dto.Responses.ApiResponse;
import org.example.dto.AppDTO;
import org.example.entity.AppEntity;
import org.example.repository.AppRepository;
import org.example.service.AppService;
import org.example.util.Messages;
import org.example.util.RequestStatus;
import org.example.util.ResponseCodes;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    AppRepository appRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    Messages messages;

    private static final Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);



    @Override
    public ApiResponse createApp(AppDTO appDTO) {
        logger.info("Create App Starts");
        AppEntity appEntity = modelMapper.map(appDTO, AppEntity.class);
        appRepository.save(appEntity);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.APP_CREATED, null));
        logger.info("App Created {} ", appEntity.getAppId());
        return apiResponse;
    }

    @Transactional
    @Override
    public ApiResponse deleteApp(int appId) {
        logger.info("Delete App starts");
        Optional<AppEntity> appEntityOptional = appRepository.findById(appId);
        ApiResponse apiResponse = new ApiResponse();

        if(appEntityOptional.isPresent()){
            appRepository.deleteAppEntityByAppId(appId);
            apiResponse.setResponseCode(ResponseCodes.SUCCESS);
            apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.APP_DELETED, null));
            logger.info("App Deleted {} ", appId);
        }else{
            apiResponse.setResponseCode(ResponseCodes.NOT_FOUND);
            apiResponse.setStatusCode(RequestStatus.NOT_FOUND.getStatusCode());
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.APP_NOT_FOUND, null));
        }
        logger.info("Delete App ends");
        return apiResponse;
    }

    @Override
    public ApiResponse getAllApps() {
        logger.info("Get Apps Starts");
        List<AppEntity> appEntities = appRepository.findNonDeletedEntities();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setAppList(appEntities);
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        logger.info("Get Apps {} ", appEntities);
        return apiResponse;
    }

    @Override
    public ApiResponse getAppById(int appId) {
        logger.info("GetById starts");
        Optional<AppEntity> appEntity = appRepository.findById(appId);
        ApiResponse apiResponse = new ApiResponse();

        if(appEntity.isPresent()){
            apiResponse.setAppEntity(appEntity.get().isDeleted() ? null : appEntity.get());
            apiResponse.setResponseCode(ResponseCodes.SUCCESS);
            apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
            apiResponse.setMessage(messages.getMessageForResponseCode(appEntity.get().isDeleted() ? ResponseCodes.APP_HAS_DELETED : ResponseCodes.APP_FOUND, null));
        }else{
            apiResponse.setResponseCode(ResponseCodes.NOT_FOUND);
            apiResponse.setStatusCode(RequestStatus.NOT_FOUND.getStatusCode());
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.APP_NOT_FOUND, null));
        }
        logger.info("GetById ends");
        return apiResponse;
    }

    @Override
    public ApiResponse updateApp(AppDTO appDTO, int appId) {
        logger.info("Update App starts");
        Optional<AppEntity> appEntityOptional = appRepository.findById(appId);
        ApiResponse apiResponse = new ApiResponse();
        if(appEntityOptional.isPresent()){
            AppEntity appEntity = appEntityOptional.get();
            appEntity.setAppName(appDTO.getAppName());
            appEntity.setWebSiteURL(appDTO.getWebSiteURL());
            appEntity.setContactNumber(appDTO.getContactNumber());
            appRepository.save(appEntity);

            apiResponse.setResponseCode(ResponseCodes.SUCCESS);
            apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.APP_UPDATED, null));
            logger.info("App Updated {} ", appEntity.getAppId());
        }else{
            apiResponse.setResponseCode(ResponseCodes.NOT_FOUND);
            apiResponse.setStatusCode(RequestStatus.NOT_FOUND.getStatusCode());
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.APP_NOT_FOUND, null));
        }
        logger.info("Update App ends");
        return apiResponse;
    }
}
