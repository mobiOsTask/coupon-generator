package org.example.repository;


import org.example.entity.AppEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppRepository extends JpaRepository<AppEntity, Integer> {

    AppEntity findByAppId(int id);

    @Modifying
    @Query("UPDATE AppEntity c SET c.isDeleted = true WHERE c.appId = :appId")
    void deleteAppEntityByAppId(@Param("appId") int appId);

    @Query("SELECT a FROM AppEntity a WHERE a.isDeleted = false")
    List<AppEntity> findNonDeletedEntities();
}
