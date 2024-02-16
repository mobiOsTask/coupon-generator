package org.example.controller;

import org.example.dto.ApiResponse;
import org.example.dto.UserDTO;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

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
}
