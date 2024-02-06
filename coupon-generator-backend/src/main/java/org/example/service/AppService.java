package org.example.service;

import org.example.dto.AppDTO;
import org.example.entity.AppEntity;

import java.util.List;

public interface AppService {

    void createApp(AppDTO appEntity);
    void deleteApp(int appId);
    List<AppEntity> getAllApps();
    void updateApp(AppDTO appDTO, int appId);
}
