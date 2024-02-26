package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_roles")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRoleEntity extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userRoleId;
    private String name;
    private String position;
    private String description;
    private String status;

    @JsonIgnore
    @OneToMany(mappedBy = "userRoleEntity", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<UserEntity> userEntities;

    @Transient
    private List<Long> pageIdList;
}
