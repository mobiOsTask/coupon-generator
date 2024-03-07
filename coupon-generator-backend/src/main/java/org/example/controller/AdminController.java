package org.example.controller;

import org.example.dto.Responses.ApiResponse;
import org.example.service.AdminService;
import org.example.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @GetMapping("/")
    public ApiResponse getAdmins(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size){
        System.out.println("In");
        Pageable pageable = PageRequest.of(page, size);
        return adminService.getAdmins(pageable);
    }

    @PostMapping("/log-in")
    public ApiResponse adminLogIn(@RequestParam(name = "name") String name, @RequestParam(name = "password") String password){
        return adminService.adminLogIn(name, password);
    }
}
