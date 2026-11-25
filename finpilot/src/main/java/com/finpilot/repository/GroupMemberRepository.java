package com.finpilot.repository;

import com.finpilot.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findByUser_UserId(Long userId);
    List<GroupMember> findByGroup_GroupId(Long groupId);

    @Query("SELECT m FROM GroupMember m WHERE m.groupMemberId = :groupMemberId AND m.user.userId = :userId")
    Optional<GroupMember> findByGroupMemberIdAndUserId(@Param("groupMemberId") Long groupMemberId, @Param("userId") Long userId);
}