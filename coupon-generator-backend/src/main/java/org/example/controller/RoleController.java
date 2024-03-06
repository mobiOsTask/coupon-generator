package org.example.controller;

import org.example.dto.Responses.ApiResponse;
import org.example.dto.RolesDTO;
import org.example.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @PostMapping("/")
    public ApiResponse addRole(@RequestBody RolesDTO rolesDTO){
        return roleService.addRole(rolesDTO);
    }

    @GetMapping("/")
    public ApiResponse getRoles(){
        return roleService.getRoles();
    }
}
