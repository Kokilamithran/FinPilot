package com.finpilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "group_members",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_group_user",
                        columnNames = {"group_id", "user_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupMemberId;

    @Column(nullable = false, length = 20)
    private String memberRole = "MEMBER";

    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedDate;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        joinedDate = LocalDateTime.now();

        if (memberRole == null) {
            memberRole = "MEMBER";
        }
    }
}