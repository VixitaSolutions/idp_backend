package com.oversoul.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oversoul.entity.ClientCompetency;
import com.oversoul.entity.EmployeeTaskDetails;
import com.oversoul.entity.User;
import com.oversoul.enums.TaskStatus;

public interface EmployeeTaskDetailsRepository extends JpaRepository<EmployeeTaskDetails, Long> {

    boolean existsByEmployeeId_IdAndCompetencyAndTaskStatusIn(User employeeId, ClientCompetency competencyId,
                                                           List<TaskStatus> statusList);

    List<EmployeeTaskDetails> findByEmployeeId_Id(long employeeId);

    List<EmployeeTaskDetails> findByEmployeeId_IdAndCompetencyAndTaskStatus(Long employeeId, Long competencyId,
                                                                         TaskStatus taskStatus);

    List<EmployeeTaskDetails> findByEmployeeId_IdAndTaskStatus(Long employeeId, TaskStatus taskStatus);

    List<EmployeeTaskDetails> findByEmployeeId_IdInAndTaskStatus(Collection<Long> ids, TaskStatus taskStatus);

    List<EmployeeTaskDetails> findByEmployeeId_IdAndCompetency(Long employeeId, Long courseType);

    List<EmployeeTaskDetails> findByCompetencyAndTaskStatus(Long competencyId, TaskStatus taskStatus);

    List<EmployeeTaskDetails> findByTaskStatus(TaskStatus taskStatus);

    List<EmployeeTaskDetails> findByCompetency(Long competencyId);

    List<EmployeeTaskDetails> findByEmployeeId_IdInAndCompetencyAndTaskStatus(List<Long> employeeIds, Long competencyId, TaskStatus taskStatus);

    List<EmployeeTaskDetails> findByEmployeeId_IdInAndCompetency(List<Long> employeeIds, Long competencyId);

    List<EmployeeTaskDetails> findByEmployeeId_IdIn(List<Long> employeeIds);
}
