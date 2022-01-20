package com.oversoul.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.oversoul.entity.UserMapping;

public interface UserMappingRepository extends JpaRepository<UserMapping, Long> {

	boolean existsByManagerIdAndCoachId(Long fromUserId, Long toUserId);

	boolean existsByManagerIdAndEmployeeId(Long fromUserId, Long toUserId);

	boolean existsByCoachIdAndEmployeeId(Long fromUserId, Long toUserId);

	@Query("select um.employeeId from UserMapping um where um.managerId=null and um.coachId = :coachId")
	List<Long> findEmployeeIdByCoachId(Long coachId);

	@Query("select um.coachId from UserMapping um where um.managerId = :managerId and um.employeeId=null")
	List<Long> findCoachIdByManagerId(Long managerId);

}
