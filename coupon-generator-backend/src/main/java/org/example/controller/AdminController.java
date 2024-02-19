package org.example.controller;

import org.example.dto.AdminDTO;
import org.example.dto.ApiResponse;
import org.example.service.AdminService;
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
    
    @PostMapping("/")
    public ApiResponse addAdmin(@RequestBody AdminDTO adminDTO){
        return adminService.addAdmin(adminDTO);
    }

    @GetMapping("/")
    public ApiResponse getAdmins(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return adminService.getAdmins(pageable);
    }

    @PostMapping("/log-in")
    public ApiResponse adminLogIn(@RequestParam(name = "name") String name, @RequestParam(name = "password") String password){
        return adminService.adminLogIn(name, password);
    }
}
