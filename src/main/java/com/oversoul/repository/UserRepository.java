package com.oversoul.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.oversoul.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String encrypt);

	Boolean existsByEmail(String email);

	@Query("select u.id from User u where u.email = :email")
	Long findIdByEmail(String email);

	List<User> findByIdIn(List<Long> employeeIds);

	Boolean existsByEmailAndTenantId(String email, UUID tenantId);

}
