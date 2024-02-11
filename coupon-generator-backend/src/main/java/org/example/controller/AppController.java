package org.example.controller;

import org.example.dto.ApiResponse;
import org.example.dto.AppDTO;
import org.example.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/app")
public class AppController {

    @Autowired
    AppService appService;

    @PostMapping("/create-app")
    public ApiResponse createApp(@RequestBody AppDTO appDTO){
        return appService.createApp(appDTO);
    }

    @GetMapping("/get-apps")
    public ApiResponse getAllApps(){
        return appService.getAllApps();
    }

    @PutMapping("/update-app/{appId}")
    public ApiResponse updateApp(@RequestBody AppDTO appDTO, @PathVariable int appId){
        return appService.updateApp(appDTO, appId);
    }

    @DeleteMapping("/delete-app/{appId}")
    public ApiResponse deleteApp(@PathVariable int appId){
        return appService.deleteApp(appId);
    }
}
