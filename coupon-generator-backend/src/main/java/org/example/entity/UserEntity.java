package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_name"),
        @UniqueConstraint(columnNames = "email")
})
public class UserEntity extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    @Column(name = "user_name")
    private String userName;
    private String email;
    private String password;
    private boolean isDeleted;
    private boolean isLoggedIn;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AdminEntity createdAdmin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_role")
    private UserRoleEntity userRoleEntity;

    public UserEntity(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
}
