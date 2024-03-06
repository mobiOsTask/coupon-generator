package org.example.controller;

import org.example.dto.Request.SignUpRequest;
import org.example.dto.Responses.ApiResponse;
import org.example.dto.Responses.MessageResponse;
import org.example.dto.UserDTO;
import org.example.entity.*;
import org.example.repository.AdminRepository;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.example.repository.UserRoleRepository;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    RoleRepository roleRepository;

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

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        if (!adminRepository.existsById(signUpRequest.getAdminId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Admin not found!"));
        }

        Optional<AdminEntity> adminEntity = adminRepository.findById(signUpRequest.getAdminId());

        RolesEntity rolesEntity = roleRepository.findByName(ERole.valueOf(signUpRequest.getRoleName()));

        if (adminEntity.isPresent()) {
            UserEntity userEntity = new UserEntity(
                    signUpRequest.getUserName(),
                    signUpRequest.getEmail(),
                    bCryptPasswordEncoder.encode(signUpRequest.getPassword())
            );

            userEntity.setCreatedAdmin(adminEntity.get());

            UserEntity savedUserEntity = userRepository.save(userEntity);

            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setRoleEntity(rolesEntity);
            userRoleEntity.setName("name");
            userRoleEntity.setPosition("position");
            userRoleEntity.setStatus("active");
            userRoleEntity.setDescription("description");
            userRoleEntity.setUserEntity(savedUserEntity);

            userRoleRepository.save(userRoleEntity);

            Optional<UserRoleEntity> userRole = userRoleRepository.findById(1);

            if (userRole.isPresent()) {
                savedUserEntity.setUserRoleEntity(userRole.get());
                userRepository.save(savedUserEntity);

                return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("UserRoleEntity with ID 1 not found"));
            }
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Admin not found"));
        }
    }

}
