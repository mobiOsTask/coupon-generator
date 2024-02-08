package org.example.service;

import org.example.dto.UserDTO;
import org.example.entity.UserEntity;

import java.util.List;

public interface UserService {
    void addUser(UserDTO userDTO);
    List<UserEntity> getUsers();
    void deleteUser(int userId);
    void updateUser(UserDTO userDTO, int userId);
}
