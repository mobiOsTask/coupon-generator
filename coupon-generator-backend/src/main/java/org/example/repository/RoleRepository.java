package org.example.repository;

import org.example.entity.ERole;
import org.example.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RolesEntity, Integer> {

    RolesEntity findByName(ERole name);
}
