package com.oversoul.service;

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
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final EmployeeTaskDetailsRepository employeeTaskDetailsRepo;

    private final EmployeeTaskHistoryRepository employeeTaskHistoryRepo;

    private final CourseRepository courseRepo;

    private final UserRepository userRepo;

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
        User employeeId = userRepo.findById(taskReq.getEmployeeId()).orElseThrow(() -> new CommonException("Invalid Employee Details"));
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
            task.setEmployeeId(employeeId);
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
        EmployeeTaskDetails task = employeeTaskDetailsRepo.findById(taskReq.getTaskId()).orElseThrow(() -> new CommonException("Task Not existed"));
        Long loggedInUserId = Long.parseLong(MDC.get("userId"));
        task.setTaskStatus(taskReq.getTaskStatus());
        employeeTaskDetailsRepo.save(task);
        createTaskHistory(taskReq, loggedInUserId, task);
        return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                "Task updated Successfully");
    }

    @Override
    public List<EmployeeTaskDetails> getAllocatedTasks(Long employeeId, TaskStatus taskStatus, Long courseType)
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
            throw new CommonException("please provide least one filter");
        }

    }

}
