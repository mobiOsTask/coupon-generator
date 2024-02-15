package org.example.controller;

import org.example.dto.ApiResponse;
import org.example.dto.AppDTO;
import org.example.service.AppService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppControllerTest {

    @Mock
    private AppService appService;

    @Mock
    private AppDTO appDTO;

    @InjectMocks
    private AppController appController;

    @Test
    void createAppTest(){
        when(appService.createApp(appDTO)).thenReturn(new ApiResponse());
        ApiResponse apiResponse = appController.createApp(appDTO);

        assertNotNull(apiResponse);
    }

    @Test
    void getAllAppsTest(){
        when(appService.getAllApps()).thenReturn(new ApiResponse());
        ApiResponse apiResponse = appController.getAllApps();

        assertNotNull(apiResponse);
    }

    @Test
    void updateAppTest(){
        int appId = 1;
        when(appService.updateApp(appDTO, appId)).thenReturn(new ApiResponse());
        ApiResponse apiResponse = appController.updateApp(appDTO, appId);

        assertNotNull(apiResponse);
    }

    @Test
    void deleteAppTest(){
        int appId = 1;
        when(appService.deleteApp(appId)).thenReturn(new ApiResponse());
        ApiResponse apiResponse = appController.deleteApp(appId);

        assertNotNull(apiResponse);
    }
}
