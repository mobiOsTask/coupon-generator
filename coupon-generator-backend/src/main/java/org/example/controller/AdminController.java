package org.example.controller;

import org.example.dto.AdminDTO;
import org.example.dto.ApiResponse;
import org.example.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;
    
    @PostMapping("/")
    public ApiResponse addAdmin(@RequestBody AdminDTO adminDTO){
        return adminService.addAdmin(adminDTO);
    }
}
