package com.finpilot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(length = 20)
    private String mobile;

    @Column(length = 3, nullable = false)
    private String currencyPreference = "INR";

    private LocalDate financialYearStart;

    @Column(nullable = false)
    private String plan = "FREE";

    @Column(nullable = false)
    private Boolean twoFaEnabled = false;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        modifiedDate = LocalDateTime.now();

        if (currencyPreference == null) {
            currencyPreference = "INR";
        }

        if (plan == null) {
            plan = "FREE";
        }

        if (twoFaEnabled == null) {
            twoFaEnabled = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }
}