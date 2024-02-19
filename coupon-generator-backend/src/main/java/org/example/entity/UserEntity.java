package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    private String userName;
    private String address;
    private String name;
    private String password;
    private boolean isDeleted;
    private boolean isLoggedIn;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AdminEntity createdAdmin;
}
