package com.oversoul.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.oversoul.entity.Course;
import com.oversoul.entity.EmployeeTaskDetails;
import com.oversoul.entity.EmployeeTaskHistory;
import com.oversoul.entity.User;
import com.oversoul.enums.TaskStatus;
import com.oversoul.exception.CommonException;
import com.oversoul.repository.CourseRepository;
import com.oversoul.repository.EmployeeTaskDetailsRepository;
import com.oversoul.repository.EmployeeTaskHistoryRepository;
import com.oversoul.repository.UserRepository;
import com.oversoul.util.ApiConstants;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.EmployeeTaskReq;

@Service
public class TaskServiceImpl implements TaskService {

	private EmployeeTaskDetailsRepository employeeTaskDetailsRepo;

	private EmployeeTaskHistoryRepository employeeTaskHistoryRepo;

	private CourseRepository courseRepo;

	private UserRepository userRepo;

	public TaskServiceImpl(EmployeeTaskDetailsRepository employeeTaskDetailsRepo,
			EmployeeTaskHistoryRepository employeeTaskHistoryRepo, CourseRepository courseRepo,
			UserRepository userRepo) {
		this.employeeTaskDetailsRepo = employeeTaskDetailsRepo;
		this.employeeTaskHistoryRepo = employeeTaskHistoryRepo;
		this.userRepo = userRepo;
		this.courseRepo = courseRepo;
	}

	@Override
	public ApiReturn createdTask(EmployeeTaskReq taskReq) throws CommonException {

		Long loggedInUserId = Long.parseLong(MDC.get("userId"));

		Course course = courseRepo.findByIdAndActive(taskReq.getCourseId(), true);
		if (course == null) {
			throw new CommonException("Invalid Course Details");
		}
		Optional<User> employeeId = userRepo.findById(taskReq.getEmployeeId());
		if (!employeeId.isPresent()) {
			throw new CommonException("Invalid Employee Details");
		}
		List<TaskStatus> statusList = new ArrayList<TaskStatus>();
		statusList.add(TaskStatus.OPEN);
		statusList.add(TaskStatus.ACCEPETED);
		statusList.add(TaskStatus.INPROGRESS);
		statusList.add(TaskStatus.COMPLETED);
		if (employeeTaskDetailsRepo.existsByEmployeeIdAndCourseTypeAndTaskStatusIn(employeeId, course, statusList)) {
			EmployeeTaskDetails task = new EmployeeTaskDetails();
			task.setCourseType(course);
			task.setCreatedBy(loggedInUserId);
			task.setDuration(taskReq.getDuration());
			task.setEmployeeId(employeeId.get());
			task.setTaskDescription(taskReq.getTaskDescription());
			task.setTaskName(taskReq.getTaskName());
			task.setTaskStatus(TaskStatus.OPEN);
			task = employeeTaskDetailsRepo.save(task);
			taskReq.setTaskStatus(TaskStatus.OPEN);
			createTaskHistory(taskReq, loggedInUserId, task);
			return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
					"Task Created Successfully");

		} else {
			throw new CommonException("Task Already existed with employee");
		}

	}

	private void createTaskHistory(EmployeeTaskReq taskReq, Long loggedInUserId, EmployeeTaskDetails task) {
		EmployeeTaskHistory taskHistory = new EmployeeTaskHistory();
		taskHistory.setComment(taskReq.getTaskDescription());
		taskHistory.setCommentedBy(loggedInUserId);
		taskHistory.setEmployeeTaskId(task.getId());
		taskHistory.setStatus(taskReq.getTaskStatus());
		employeeTaskHistoryRepo.save(taskHistory);
	}

	@Override
	public ApiReturn updateTask(EmployeeTaskReq taskReq) throws CommonException {
		Optional<EmployeeTaskDetails> task = employeeTaskDetailsRepo.findById(taskReq.getTaskId());
		if (!task.isPresent()) {
			Long loggedInUserId = Long.parseLong(MDC.get("userId"));
			EmployeeTaskDetails employeeTaskDetails = task.get();
			employeeTaskDetails.setTaskStatus(taskReq.getTaskStatus());
			employeeTaskDetailsRepo.save(employeeTaskDetails);
			createTaskHistory(taskReq, loggedInUserId, task.get());
			return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
					"Task updated Successfully");

		}

		throw new CommonException("Task Not existed");

	}

	@Override
	public List<EmployeeTaskDetails> getAllotedTasks(Long employeeId, TaskStatus taskStatus, Long courseType)
			throws CommonException {

		if (employeeId != null && employeeId != 0 && taskStatus != null && courseType != null && courseType != 0) {
			return employeeTaskDetailsRepo.findByEmployeeIdAndCourseTypeAndTaskStatus(employeeId, courseType,
					taskStatus);
		} else if (employeeId != null && employeeId != 0 && taskStatus != null) {
			return employeeTaskDetailsRepo.findByEmployeeIdAndTaskStatus(employeeId, taskStatus);
		} else if (employeeId != null && employeeId != 0 && courseType != null && courseType != 0) {
			return employeeTaskDetailsRepo.findByEmployeeIdAndCourseType(employeeId, courseType);
		} else if (taskStatus != null && courseType != null && courseType != 0) {
			return employeeTaskDetailsRepo.findByCourseTypeAndTaskStatus(courseType, taskStatus);
		} else if (employeeId != null && employeeId != 0) {
			return employeeTaskDetailsRepo.findByEmployeeId(employeeId);
		} else if (taskStatus != null) {
			return employeeTaskDetailsRepo.findByTaskStatus(taskStatus);
		} else if (courseType != null && courseType != 0) {
			return employeeTaskDetailsRepo.findByCourseType(courseType);
		} else {
			throw new CommonException("please provide atleast one filtere");

		}

	}

}
