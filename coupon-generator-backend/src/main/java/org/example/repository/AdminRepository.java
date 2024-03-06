package org.example.repository;

import org.example.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {

    @Query("SELECT a FROM AdminEntity a WHERE a.name =:name")
    List<AdminEntity> adminLogIn(@Param("name") String name);

    @Modifying
    @Query("UPDATE AdminEntity a SET a.isLogIn = true WHERE a.adminId =:adminId")
    void updateLogIn(@Param("adminId") int adminId);

    boolean existsByName(String userName);
    boolean existsByAddress(String address);

}
