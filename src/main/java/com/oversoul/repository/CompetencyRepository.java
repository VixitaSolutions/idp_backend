package com.oversoul.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oversoul.entity.ClientCompetency;

@Repository
public interface CompetencyRepository extends JpaRepository<ClientCompetency, Long> {
	
	List<ClientCompetency> findByTenantId(@Param("tenantId") UUID tenantId);

}
