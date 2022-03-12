package com.oversoul.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oversoul.entity.UserMapping;

public interface UserMappingRepository extends JpaRepository<UserMapping, Long> {
	
	UserMapping findByManagerIdAndCoachId(Long managerId, Long coachId);

	boolean existsByManagerIdAndCoachId(Long fromUserId, Long toUserId);

	boolean existsByManagerIdAndEmployeeId(Long fromUserId, Long toUserId);

	boolean existsByCoachIdAndEmployeeId(Long fromUserId, Long toUserId);

	@Query("select um.employeeId from UserMapping um where um.managerId=null and um.coachId = :coachId and active = true")
	List<Long> findEmployeeIdByCoachId(@Param("coachId") Long coachId);

	@Query("select um.coachId from UserMapping um where um.managerId = :managerId and um.employeeId=null and active = true")
	List<Long> findCoachIdByManagerId(@Param("managerId") Long managerId);
	
	@Query("select um.employeeId from UserMapping um where um.managerId = :managerId and um.employeeId!=null and active = true")
	List<Long> findEmployeeIdByManagerId(@Param("managerId") Long managerId);
	
	@Query("select um from UserMapping um where um.managerId = :managerId and um.coachId = :coachId and um.employeeId= :employeeId")
	UserMapping findByManagerIdAndCoachIdAndEmployeeId(@Param("managerId") Long managerId, @Param("coachId") Long coachId, @Param("employeeId") Long employeeId);

}
