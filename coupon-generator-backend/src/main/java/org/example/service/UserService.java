package org.example.service;

import org.example.dto.ApiResponse;
import org.example.dto.UserDTO;
import org.example.entity.UserEntity;

import java.util.List;

public interface UserService {
    ApiResponse addUser(UserDTO userDTO, int adminId);
    ApiResponse getUsers();
    ApiResponse getUserById(int userId);
    ApiResponse deleteUser(int userId);
    ApiResponse updateUser(UserDTO userDTO, int userId);
}
