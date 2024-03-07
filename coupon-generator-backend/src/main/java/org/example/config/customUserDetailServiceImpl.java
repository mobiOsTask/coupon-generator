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
public class customUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userInfo = repository.findByUserName(username);
        return userInfo.map(customUserDetail::new)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found " + username));
    }

}
