package com.oversoul.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oversoul.entity.Course;
import com.oversoul.entity.EmployeeTaskDetails;
import com.oversoul.entity.User;
import com.oversoul.enums.TaskStatus;

public interface EmployeeTaskDetailsRepository extends JpaRepository<EmployeeTaskDetails, Long> {

	boolean existsByEmployeeIdAndCourseTypeAndTaskStatusIn(Optional<User> employeeId, Course course,
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
