package com.oversoul.repository;

import com.oversoul.entity.Course;
import com.oversoul.entity.EmployeeTaskDetails;
import com.oversoul.entity.User;
import com.oversoul.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeTaskDetailsRepository extends JpaRepository<EmployeeTaskDetails, Long> {

    boolean existsByEmployeeIdAndCourseTypeAndTaskStatusIn(User employeeId, Course course,
                                                           List<TaskStatus> statusList);

    List<EmployeeTaskDetails> findByEmployeeId(long employeeId);

    List<EmployeeTaskDetails> findByEmployeeIdAndCourseTypeAndTaskStatus(Long employeeId, Long courseType,
                                                                         TaskStatus taskStatus);

    List<EmployeeTaskDetails> findByEmployeeIdAndTaskStatus(Long employeeId, TaskStatus taskStatus);

    List<EmployeeTaskDetails> findByEmployeeIdAndCourseType(Long employeeId, Long courseType);

    List<EmployeeTaskDetails> findByCourseTypeAndTaskStatus(Long courseType, TaskStatus taskStatus);

    List<EmployeeTaskDetails> findByTaskStatus(TaskStatus taskStatus);

    List<EmployeeTaskDetails> findByCourseType(Long courseType);

}
