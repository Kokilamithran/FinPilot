package com.finpilot.repository;

import com.finpilot.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByCreatedBy_UserId(Long userId);
    Optional<Group> findByGroupIdAndCreatedBy_UserId(Long groupId, Long userId);

    @Query("SELECT DISTINCT g FROM Group g LEFT JOIN GroupMember gm ON gm.group = g WHERE g.createdBy.userId = :userId OR gm.user.userId = :userId")
    List<Group> findGroupsByUserIdOrMemberUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT g FROM Group g LEFT JOIN GroupMember gm ON gm.group = g WHERE g.groupId = :groupId AND (g.createdBy.userId = :userId OR gm.user.userId = :userId)")
    Optional<Group> findByGroupIdAndUserOrMember(@Param("groupId") Long groupId, @Param("userId") Long userId);
}