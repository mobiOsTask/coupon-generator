package org.example.service.impl;

import org.example.dto.ApiResponse;
import org.example.dto.AppDTO;
import org.example.entity.AppEntity;
import org.example.repository.AppRepository;
import org.example.util.RequestStatus;
import org.example.util.ResponseCodes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppServiceTest {

    @Mock
    AppRepository appRepository;

    @Mock
    ModelMapper modelMapper;

    @Mock
    AppDTO appDTO;

    @Mock
    AppEntity appEntity;

    @Mock
    List<AppEntity> appEntities;

    @InjectMocks
    AppServiceImpl appService;

    @Test
    void createAppTest(){
        when(modelMapper.map(appDTO, AppEntity.class)).thenReturn(appEntity);
        when(appRepository.save(any())).thenReturn(appEntity);

        ApiResponse apiResponse = appService.createApp(appDTO);

        assertNotNull(apiResponse);
        assertEquals(ResponseCodes.SUCCESS, apiResponse.getResponseCode());
        assertEquals(RequestStatus.SUCCESS.getStatusCode(), apiResponse.getStatusCode());
        assertEquals("App Created Successfully", apiResponse.getMessage());
    }

    @Test
    void getAllAppsTest(){
        when(appRepository.findAll()).thenReturn(appEntities);

        ApiResponse apiResponse = appService.getAllApps();

        assertNotNull(apiResponse);
        assertEquals(ResponseCodes.SUCCESS, apiResponse.getResponseCode());
        assertEquals(RequestStatus.SUCCESS.getStatusCode(), apiResponse.getStatusCode());
        assertNotNull(apiResponse.getAppList());
    }

    @Test
    void updateAppTest(){
        int appId = 1;
        when(appRepository.findById(appId)).thenReturn(Optional.of(appEntity));

        ApiResponse apiResponse = appService.updateApp(appDTO, appId);

        assertNotNull(apiResponse);
        assertEquals(ResponseCodes.SUCCESS, apiResponse.getResponseCode());
        assertEquals(RequestStatus.SUCCESS.getStatusCode(), apiResponse.getStatusCode());
        assertEquals("App Updated Successfully", apiResponse.getMessage());
    }

    @Test
    void deleteAppTest(){
        int appId = 1;
        doNothing().when(appRepository).deleteById(appId);

        ApiResponse apiResponse = appService.deleteApp(appId);

        assertNotNull(apiResponse);
        assertEquals(ResponseCodes.SUCCESS, apiResponse.getResponseCode());
        assertEquals(RequestStatus.SUCCESS.getStatusCode(), apiResponse.getStatusCode());
        assertEquals("App Deleted Successfully", apiResponse.getMessage());
    }
}
