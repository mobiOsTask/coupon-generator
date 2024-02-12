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

    @PostMapping("/add-user")
    public ApiResponse addUser(@RequestBody UserDTO userDTO){
        return userService.addUser(userDTO);
    }

    @GetMapping("/get-users")
    public ApiResponse getAllUsers(){
        return userService.getUsers();
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
