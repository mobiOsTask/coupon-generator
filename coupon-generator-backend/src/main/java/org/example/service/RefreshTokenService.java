package org.example.service;

import org.example.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshTokenEntity createRefreshToken(int userId);

    Optional<RefreshTokenEntity> findByToken(String token);

    RefreshTokenEntity verifyExpiration(RefreshTokenEntity refreshTokenEntity);

    void deleteRefreshToken(String token);
}
