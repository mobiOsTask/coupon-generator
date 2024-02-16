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

    @Column(nullable = false)
    private LocalDate usedDate;

    @Column(nullable = false)
    private LocalTime usedTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private UserEntity coupon;
}
