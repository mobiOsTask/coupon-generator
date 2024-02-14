package org.example.service.impl;

import org.example.dto.ApiResponse;
import org.example.dto.UserDTO;
import org.example.entity.CouponEntity;
import org.example.entity.UserEntity;
import org.example.repository.CouponRepository;
import org.example.repository.UserRepository;
import org.example.service.UserService;
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
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    Messages messages;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public ApiResponse addUser(UserDTO userDTO) {
        logger.info("Add User Starts");
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        userRepository.save(userEntity);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.USER_CREATED, null));
        logger.info("User Added {} " , userEntity.getUserId());
        return apiResponse;
    }

    @Override
    public ApiResponse getUsers() {
        logger.info("Get Users starts");
        List<UserEntity> userEntities = userRepository.findAll();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setUserList(userEntities);
        apiResponse.setResponseCode(ResponseCodes.SUCCESS);
        apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        logger.info("Get Users {} " , userEntities);
        return apiResponse;
    }

    @Override
    public ApiResponse getUserById(int userId) {
        logger.info("Get UserById starts");

        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);

        ApiResponse apiResponse = new ApiResponse();
        if(optionalUserEntity.isPresent()){
            UserEntity userEntity = optionalUserEntity.get();
            apiResponse.setUserEntity(userEntity);
            apiResponse.setResponseCode(ResponseCodes.SUCCESS);
            apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
        }else {
            apiResponse.setResponseCode(ResponseCodes.NOT_FOUND);
            apiResponse.setStatusCode(RequestStatus.NOT_FOUND.getStatusCode());
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.USER_NOT_FOUND, null));
        }
        logger.info("Get UserById ends");
        return apiResponse;
    }

    @Override
    public ApiResponse deleteUser(int userId) {
        logger.info("Delete User starts");
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        ApiResponse apiResponse = new ApiResponse();

        if(optionalUserEntity.isPresent()){
            userRepository.deleteById(userId);
            apiResponse.setResponseCode(ResponseCodes.SUCCESS);
            apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.USER_DELETED, null));
            logger.info("User Deleted {} " , userId);
        }else{
            apiResponse.setResponseCode(ResponseCodes.NOT_FOUND);
            apiResponse.setStatusCode(RequestStatus.NOT_FOUND.getStatusCode());
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.USER_NOT_FOUND, null));
        }
        logger.info("Delete User ends");
        return apiResponse;
    }

    @Override
    public ApiResponse updateUser(UserDTO userDTO, int userId) {
        logger.info("Update User starts");
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        ApiResponse apiResponse = new ApiResponse();

        if(optionalUserEntity.isPresent()){
            UserEntity userEntity = optionalUserEntity.get();
            userEntity.setUserName(userDTO.getUserName());
            userEntity.setAddress(userDTO.getAddress());
            userRepository.save(userEntity);
            apiResponse.setResponseCode(ResponseCodes.SUCCESS);
            apiResponse.setStatusCode(RequestStatus.SUCCESS.getStatusCode());
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.USER_UPDATED, null));
            logger.info("User Updated {} " , userEntity.getUserId());
        }else{
            apiResponse.setResponseCode(ResponseCodes.NOT_FOUND);
            apiResponse.setStatusCode(RequestStatus.NOT_FOUND.getStatusCode());
            apiResponse.setMessage(messages.getMessageForResponseCode(ResponseCodes.USER_NOT_FOUND, null));
            logger.info("User Not Updated");
        }
        logger.info("Update User ends");
        return apiResponse;
    }

    public void saveCoupons(List<CouponEntity> data){

    }
}
