package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.Request.SignUpRequest;
import org.example.dto.Responses.ApiResponse;
import org.example.dto.Responses.MessageResponse;
import org.example.dto.UserDTO;
import org.example.entity.*;
import org.example.repository.AdminRepository;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.example.repository.UserRoleRepository;
import org.example.service.RefreshTokenService;
import org.example.service.UserService;
import org.example.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/")
    public ApiResponse addUser(@RequestBody UserDTO userDTO, @RequestParam("adminId") int adminId){
        return userService.addUser(userDTO, adminId);
    }

    @GetMapping("/")
    public ApiResponse getAllUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public ApiResponse getUserById(@PathVariable int userId){
        return userService.getUserById(userId);
    }

    @DeleteMapping("/delete-user/{userId}")
    public ApiResponse deleteUser(@PathVariable int userId){
        return userService.deleteUser(userId);
    }

    @PutMapping("/update-user/{userId}")
    public ApiResponse updateApp(@RequestBody UserDTO userDTO, @PathVariable int userId){
        return userService.updateUser(userDTO, userId);
    }
    @PostMapping("/log-in")
    public ApiResponse adminLogIn(@RequestParam(name = "name") String name, @RequestParam(name = "password") String password){
        return userService.userLogin(name, password);
    }
}
