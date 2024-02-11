package org.example.service;

import org.example.dto.ApiResponse;
import org.example.dto.AppDTO;
import org.example.entity.AppEntity;

import java.util.List;

public interface AppService {

    ApiResponse createApp(AppDTO appEntity);
    ApiResponse deleteApp(int appId);
    ApiResponse getAllApps();
    ApiResponse updateApp(AppDTO appDTO, int appId);
}
