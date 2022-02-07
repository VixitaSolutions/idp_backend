package com.oversoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oversoul.entity.Role;
import com.oversoul.entity.UserRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.lang.NonNull;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

	UserRole findByUserId(Long id);

	boolean existsByUserIdAndRoleId(Long coachId, Role valueOf);

	@NonNull
	UserRole findByUserIdAndRoleId_Id(Long userId, Long id);

	@Modifying
	long deleteByUserIdAndRoleId(Long userId, Role id);
}
