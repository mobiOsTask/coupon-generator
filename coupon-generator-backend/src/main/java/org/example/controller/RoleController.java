package org.example.controller;

import org.example.dto.ApiResponse;
import org.example.dto.RolesDTO;
import org.example.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @PostMapping("/")
    public ApiResponse addRole(@RequestBody RolesDTO rolesDTO){
        return roleService.addRole(rolesDTO);
    }
}
