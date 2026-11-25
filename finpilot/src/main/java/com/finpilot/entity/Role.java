package com.finpilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(nullable = false, unique = true, length = 50)
    private String roleName;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private String createdBy;

    private String modifiedBy;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        modifiedDate = LocalDateTime.now();

        if (createdBy == null) {
            createdBy = "system";
        }

        if (modifiedBy == null) {
            modifiedBy = "system";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();

        if (modifiedBy == null) {
            modifiedBy = "system";
        }
    }
}