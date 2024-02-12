package org.example.repository;


import org.example.entity.AppEntity;
import org.example.service.AppService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRepository extends JpaRepository<AppEntity, Integer> {

    AppEntity findByAppId(int id);
}
