package org.example.repository;

import org.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Modifying
    @Query("UPDATE UserEntity c SET c.isDeleted = true WHERE c.userId = :userId")
    void deleteAppEntityByUserId(@Param("userId") int userId);

    @Query("SELECT a FROM UserEntity a WHERE a.isDeleted = false")
    List<UserEntity> findNonDeletedEntities();
}
