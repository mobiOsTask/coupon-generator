package org.example.controller;

import org.example.dto.UserDTO;
import org.example.entity.UserEntity;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO){
        userService.addUser(userDTO);
        return new ResponseEntity<>("User Added", HttpStatus.OK);
    }

    @GetMapping("/get-all-users")
    public List<UserEntity> getAllUsers(){
        return userService.getUsers();
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>("User Deleted", HttpStatus.OK);
    }

    @PutMapping("/update-user/{userId}")
    public ResponseEntity<String> updateApp(@RequestBody UserDTO userDTO, @PathVariable int userId){
        userService.updateUser(userDTO, userId);
        return new ResponseEntity<>("User Updated", HttpStatus.OK);
    }
}
