package org.example.controller;

import org.example.dto.AppDTO;
import org.example.entity.AppEntity;
import org.example.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/app")
public class AppController {

    @Autowired
    AppService appService;

    @PostMapping("/create-app")
    public ResponseEntity<String> createApp(@RequestBody AppDTO appDTO){
        appService.createApp(appDTO);
        return new ResponseEntity<>("App Created", HttpStatus.OK);
    }

    @GetMapping("/get-all-apps")
    public ResponseEntity<List<AppEntity>> getAllApps(){
        List<AppEntity> appEntities = appService.getAllApps();
        return new ResponseEntity<>(appEntities, HttpStatus.OK);
    }

    @PutMapping("/update-app/{appId}")
    public ResponseEntity<String> updateApp(@RequestBody AppDTO appDTO, @PathVariable int appId){
        appService.updateApp(appDTO, appId);
        return new ResponseEntity<>("App Updated", HttpStatus.OK);
    }

    @DeleteMapping("/delete-app/{appId}")
    public ResponseEntity<String> deleteApp(@PathVariable int appId){
        appService.deleteApp(appId);
        return new ResponseEntity<>("App Deleted : "+appId, HttpStatus.OK);
    }
}
