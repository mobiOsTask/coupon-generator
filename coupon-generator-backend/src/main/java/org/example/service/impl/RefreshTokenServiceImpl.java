package org.example.service.impl;

import org.example.entity.RefreshTokenEntity;
import org.example.entity.UserEntity;
import org.example.exception.TokenRefreshException;
import org.example.repository.AdminRepository;
import org.example.repository.RefreshTokenRepository;
import org.example.repository.UserRepository;
import org.example.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminRepository adminRepository;

    @Override
    public RefreshTokenEntity createRefreshToken(String userName){
        Optional<UserEntity> userEntity = userRepository.findByUserName(userName);
        if (userEntity.isPresent()){
            RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .userEntity(userRepository.findByUserName(userName).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plus(5, ChronoUnit.HOURS))
                .build();
            return refreshTokenRepository.save(refreshTokenEntity);
        }else {
            RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                    .adminEntity(adminRepository.findByUserName(userName).get())
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plus(5, ChronoUnit.HOURS))
                    .build();
            return refreshTokenRepository.save(refreshTokenEntity);
        }


    }

    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity refreshTokenEntity){
        if(refreshTokenEntity.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refreshTokenEntity);
            throw new TokenRefreshException(refreshTokenEntity.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return refreshTokenEntity;
    }

    @Override
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

}
