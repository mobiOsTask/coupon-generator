package org.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.example.controller.CouponController;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @JsonIgnore
    @Column(name="uuid")
    private String uuid;

    @JsonIgnore
    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    @JsonIgnore
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_datetime")
    private LocalDateTime createdDatetime;

    @JsonIgnore
    @LastModifiedBy
    @Column(name = "modified_by")
    private Long modifiedBy;

    @JsonIgnore
    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_datetime")
    private LocalDateTime modifiedDatetime;

    @JsonIgnore
    @Version
    @Column(name = "version")
    private Integer version;

    @PrePersist
    public void onCreate() {
        this.createdDatetime = Timestamp.valueOf(LocalDateTime.now(ZoneId.of(CouponController.TIME_ZONE))).toLocalDateTime();
        this.modifiedDatetime = Timestamp.valueOf(LocalDateTime.now(ZoneId.of(CouponController.TIME_ZONE))).toLocalDateTime();
    }

    @PreUpdate
    public void onUpdate() {
        this.modifiedDatetime = Timestamp.valueOf(LocalDateTime.now(ZoneId.of(CouponController.TIME_ZONE))).toLocalDateTime();
    }
}
