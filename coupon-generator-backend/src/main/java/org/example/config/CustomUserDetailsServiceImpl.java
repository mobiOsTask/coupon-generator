package org.example.config;

import org.example.entity.AdminEntity;
import org.example.entity.UserEntity;
import org.example.exception.DLAppValidationsException;
import org.example.repository.AdminRepository;
import org.example.repository.UserRepository;
import org.example.util.Messages;
import org.example.util.ResponseCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String username){
        Optional<AdminEntity> adminInfo =  adminRepository.findByUserName(username);
        Optional<UserEntity> userInfo = userRepository.findByUserName(username);
        if (adminInfo.isPresent()) {
            return new CustomUserDetail(adminInfo.get());
        } else if (userInfo.isPresent()) {
            return new CustomUserDetail(userInfo.get());
        }
        throw new DLAppValidationsException(ResponseCodes.UNAUTHORIZED,"username or password");
    }


}
