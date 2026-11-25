package com.finpilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, length = 100)
    private String categoryName;

    @Column(nullable = false, length = 20)
    private String categoryType;

    @Column(length = 50)
    private String icon;

    @Column(length = 7)
    private String colour;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @Column(nullable = false)
    private Boolean isSystem = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer displayOrder;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        modifiedDate = LocalDateTime.now();

        if (isSystem == null) {
            isSystem = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
        if (isSystem == null) {
            isSystem = false;
        }
    }
}