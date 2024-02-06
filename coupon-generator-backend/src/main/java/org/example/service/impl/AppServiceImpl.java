package org.example.service.impl;

import org.example.repository.AppRepository;
import org.example.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    AppRepository appRepository;
}
