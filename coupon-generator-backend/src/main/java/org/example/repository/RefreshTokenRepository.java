package org.example.repository;

import org.example.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {

    @Query("SELECT t FROM RefreshTokenEntity t WHERE t.token=:token")
    Optional<RefreshTokenEntity> findByToken(String token);
}
