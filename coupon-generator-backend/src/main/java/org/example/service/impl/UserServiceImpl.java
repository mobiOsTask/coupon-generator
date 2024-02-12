package org.example.service.impl;

import org.example.dto.ApiResponse;
import org.example.dto.UserDTO;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.util.RequestStatus;
import org.example.util.ResponseCodes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ApiResponse addUser(UserDTO userDTO) {
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        userRepository.save(userEntity);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage("User Added Successfully");
        return apiResponse;
    }

    @Override
    public ApiResponse getUsers() {
        List<UserEntity> userEntities = userRepository.findAll();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setUserList(userEntities);
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        return apiResponse;
    }

    @Override
    public ApiResponse deleteUser(int userId) {
        userRepository.deleteById(userId);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage("User Deleted Successfully");
        return apiResponse;
    }

    @Override
    public ApiResponse updateUser(UserDTO userDTO, int userId) {
        UserEntity userEntity = userRepository.findById(userId).get();

        userEntity.setUserName(userDTO.getUserName());
        userEntity.setAddress(userDTO.getAddress());
        userRepository.save(userEntity);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage("User Updated Successfully");
        return apiResponse;
    }
}
