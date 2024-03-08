package org.example.service;

import org.example.dto.Responses.ApiResponse;
import org.example.dto.UserDTO;

public interface UserService {
    ApiResponse signUpUser(UserDTO userDTO, int adminId);
    ApiResponse getUsers();
    ApiResponse getUserById(int userId);
    ApiResponse deleteUser(int userId);
    ApiResponse updateUser(UserDTO userDTO, int userId);

    ApiResponse userLogin(String name, String password);
}
