package com.oversoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oversoul.entity.Role;
import com.oversoul.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

	// @Query(name = "select ur from UserRole ur where ur.userId.id = :id")
	UserRole findByUserId(Long id);

	boolean existsByUserIdAndRoleId(Long coachId, Role valueOf);

}
