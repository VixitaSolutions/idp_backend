package com.oversoul.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oversoul.entity.ClientCompetency;
import com.oversoul.entity.EmployeeTaskDetails;
import com.oversoul.entity.User;
import com.oversoul.enums.TaskStatus;

public interface EmployeeTaskDetailsRepository extends JpaRepository<EmployeeTaskDetails, Long> {

    boolean existsByEmployeeIdAndCompetencyAndTaskStatusIn(User employeeId, ClientCompetency competencyId,
                                                           List<TaskStatus> statusList);

    List<EmployeeTaskDetails> findByEmployeeId(long employeeId);

    List<EmployeeTaskDetails> findByEmployeeIdAndCompetencyAndTaskStatus(Long employeeId, Long competencyId,
                                                                         TaskStatus taskStatus);

    List<EmployeeTaskDetails> findByEmployeeIdAndTaskStatus(Long employeeId, TaskStatus taskStatus);

    List<EmployeeTaskDetails> findByEmployeeIdAndCompetency(Long employeeId, Long courseType);

    List<EmployeeTaskDetails> findByCompetencyAndTaskStatus(Long competencyId, TaskStatus taskStatus);

    List<EmployeeTaskDetails> findByTaskStatus(TaskStatus taskStatus);

    List<EmployeeTaskDetails> findByCompetency(Long competencyId);

}
