package com.finpilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "import_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long importId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false, length = 10)
    private String fileFormat;

    @Column(nullable = false, updatable = false)
    private LocalDateTime importedAt;

    @Column(nullable = false)
    private Integer totalRecords = 0;

    @Column(nullable = false)
    private Integer successCount = 0;

    @Column(nullable = false)
    private Integer duplicateCount = 0;

    @Column(nullable = false, length = 20)
    private String status = "COMPLETED";

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @PrePersist
    protected void onCreate() {

        if (importedAt == null) {
            importedAt = LocalDateTime.now();
        }

        if (totalRecords == null) {
            totalRecords = 0;
        }

        if (successCount == null) {
            successCount = 0;
        }

        if (duplicateCount == null) {
            duplicateCount = 0;
        }

        if (status == null) {
            status = "COMPLETED";
        }
    }
}