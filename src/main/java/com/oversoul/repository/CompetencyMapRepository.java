package com.oversoul.repository;

import com.oversoul.entity.ClientCompetency;
import com.oversoul.entity.CompetencyMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CompetencyMapRepository extends JpaRepository<CompetencyMap, Long>{
}
