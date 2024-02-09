package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon_user")
public class CouponUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int usedCouponId;
    private String number;
    private LocalDate usedDate;
    private LocalTime usedTime;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
