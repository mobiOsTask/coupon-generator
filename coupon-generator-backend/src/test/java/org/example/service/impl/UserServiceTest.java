package org.example.service.impl;

import org.example.dto.ApiResponse;
import org.example.dto.UserDTO;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    ModelMapper modelMapper;

    @Mock
    UserDTO userDTO;

    @Mock
    UserEntity userEntity;

    @Mock
    List<UserEntity> userEntities;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void addUserTest(){
        int adminId = 1;
        when(modelMapper.map(userDTO, UserEntity.class)).thenReturn(userEntity);
        when(userRepository.save(any())).thenReturn(userEntity);

        ApiResponse apiResponse = userService.addUser(userDTO, adminId);

        assertNotNull(apiResponse);
        assertEquals(ResponseCodes.SUCCESS, apiResponse.getResponseCode());
        assertEquals(RequestStatus.SUCCESS.getStatusCode(), apiResponse.getStatusCode());
        assertEquals("User Added Successfully", apiResponse.getMessage());
    }

    @Test
    void getAllUsersTest(){
        when(userRepository.findAll()).thenReturn(userEntities);

        ApiResponse apiResponse = userService.getUsers();

        assertNotNull(apiResponse);
        assertEquals(ResponseCodes.SUCCESS, apiResponse.getResponseCode());
        assertEquals(RequestStatus.SUCCESS.getStatusCode(), apiResponse.getStatusCode());
        assertNotNull(apiResponse.getUserList());
    }

    @Test
    void updateUserTest(){
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        ApiResponse apiResponse = userService.updateUser(userDTO, userId);

        assertNotNull(apiResponse);
        assertEquals(ResponseCodes.SUCCESS, apiResponse.getResponseCode());
        assertEquals(RequestStatus.SUCCESS.getStatusCode(), apiResponse.getStatusCode());
        assertEquals("User Updated Successfully", apiResponse.getMessage());
    }

    @Test
    void deleteUserTest(){
        int userId = 1;
        doNothing().when(userRepository).deleteById(userId);

        ApiResponse apiResponse = userService.deleteUser(userId);

        assertNotNull(apiResponse);
        assertEquals(ResponseCodes.SUCCESS, apiResponse.getResponseCode());
        assertEquals(RequestStatus.SUCCESS.getStatusCode(), apiResponse.getStatusCode());
        assertEquals("User Deleted Successfully", apiResponse.getMessage());
    }
}
