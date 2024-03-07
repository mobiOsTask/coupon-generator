package org.example.config;


import org.example.entity.AdminEntity;
import org.example.entity.UserEntity;
import org.example.repository.AdminRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class customAdminDetailServiceImpl implements UserDetailsService {

    @Autowired
    private AdminRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AdminEntity> userInfo = repository.findByUserName(username);
        return userInfo.map(customAdminDetail::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));

    }
}
