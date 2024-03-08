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
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AdminEntity> adminInfo =  adminRepository.findByUserName(username);
        if (adminInfo.isPresent()) {
            System.out.println(adminInfo);
            return adminInfo.map(CustomUserDetail::new)
                    .orElseThrow(() -> new UsernameNotFoundException("Admin not found " + username));
        } else {
            Optional<UserEntity> userInfo = userRepository.findByUserName(username);
            System.out.println(userInfo);
            return userInfo.map(CustomUserDetail::new)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
        }

    }


}
