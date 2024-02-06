package org.example.service.impl;

import org.example.dto.AppDTO;
import org.example.entity.AppEntity;
import org.example.repository.AppRepository;
import org.example.service.AppService;
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
    public void createApp(AppDTO appDTO) {
        AppEntity appEntity = modelMapper.map(appDTO, AppEntity.class);
        appRepository.save(appEntity);
    }

    @Override
    public void deleteApp(int appId) {
        appRepository.deleteById(appId);
    }

    @Override
    public List<AppEntity> getAllApps() {
        return appRepository.findAll();
    }

    @Override
    public void updateApp(AppDTO appDTO, int appId) {
        AppEntity appEntity = appRepository.findById(appId).get();
        appEntity.setAppId(appDTO.getAppId());
        appEntity.setAppName(appDTO.getAppName());
        appEntity.setWebSiteURL(appDTO.getWebSiteURL());
        appEntity.setContactNumber(appDTO.getContactNumber());

        appRepository.save(appEntity);
    }
}
