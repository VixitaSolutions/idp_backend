package com.oversoul.repository;

import com.oversoul.entity.ClientCompetency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ClientCompetencyRepository extends JpaRepository<ClientCompetency, Long> {

    List<ClientCompetency> findByTenantId(@Param("tenantId") UUID tenantId);

    ClientCompetency findByNameIgnoreCaseAndLevelAndTenantId(String name, Long level, UUID tenantId);

}
