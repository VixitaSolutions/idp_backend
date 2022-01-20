package com.oversoul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oversoul.entity.EmployeeTaskHistory;

@Repository
public interface EmployeeTaskHistoryRepository extends JpaRepository<EmployeeTaskHistory, Long> {

}
