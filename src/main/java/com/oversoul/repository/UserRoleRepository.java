package com.oversoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oversoul.entity.Role;
import com.oversoul.entity.UserRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

	UserRole findByUserId(Long id);

	boolean existsByUserIdAndRoleId(Long coachId, Role valueOf);

	@NonNull
	UserRole findByUserIdAndRoleId_Id(Long userId, Long id);

	@Modifying
	long deleteByUserIdAndRoleId(Long userId, Role id);

    boolean existsByUserId(Long userId);


	@Query(value = "select u.id from User u join  user_role ur ON ur.user_id=u.id where u.tenant_id = :tenantId",nativeQuery = true)
	List<Long> findIdsByTenantId(@Param("tenantId") UUID tenantId);
}
