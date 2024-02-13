package org.example.service.impl;

import org.example.dto.ApiResponse;
import org.example.dto.UserDTO;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.util.RequestStatus;
import org.example.util.ResponseCodes;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public ApiResponse addUser(UserDTO userDTO) {
        logger.info("Add User Starts");
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        userRepository.save(userEntity);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage("User Added Successfully");
        logger.info("User Added {} " , userEntity.getUserId());
        return apiResponse;
    }

    @Override
    public ApiResponse getUsers() {
        logger.info("Get Users");
        List<UserEntity> userEntities = userRepository.findAll();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setUserList(userEntities);
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        logger.info("Get Users {} " , userEntities);
        return apiResponse;
    }

    @Override
    public ApiResponse deleteUser(int userId) {
        logger.info("Delete User Starts");
        userRepository.deleteById(userId);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage("User Deleted Successfully");
        logger.info("User Deleted {} " , userId);
        return apiResponse;
    }

    @Override
    public ApiResponse updateUser(UserDTO userDTO, int userId) {
        logger.info("Update User Starts");
        UserEntity userEntity = userRepository.findById(userId).get();

        userEntity.setUserName(userDTO.getUserName());
        userEntity.setAddress(userDTO.getAddress());
        userRepository.save(userEntity);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage("User Updated Successfully");
        logger.info("User Updated {} " , userEntity.getUserId());
        return apiResponse;
    }
}
