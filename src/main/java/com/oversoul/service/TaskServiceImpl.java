package com.oversoul.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.oversoul.entity.ClientCompetency;
import com.oversoul.entity.EmployeeTaskDetails;
import com.oversoul.entity.EmployeeTaskHistory;
import com.oversoul.entity.User;
import com.oversoul.enums.TaskStatus;
import com.oversoul.exception.CommonException;
import com.oversoul.repository.CompetencyRepository;
import com.oversoul.repository.CourseRepository;
import com.oversoul.repository.EmployeeTaskDetailsRepository;
import com.oversoul.repository.EmployeeTaskHistoryRepository;
import com.oversoul.repository.UserRepository;
import com.oversoul.util.ApiConstants;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.EmployeeTaskReq;

@Service
public class TaskServiceImpl implements TaskService {

    private final EmployeeTaskDetailsRepository employeeTaskDetailsRepo;

    private final EmployeeTaskHistoryRepository employeeTaskHistoryRepo;

    private final CourseRepository courseRepo;
    
    private final CompetencyRepository competencyRepo;

    private final UserRepository userRepo;

    public TaskServiceImpl(EmployeeTaskDetailsRepository employeeTaskDetailsRepo,
                           EmployeeTaskHistoryRepository employeeTaskHistoryRepo, CourseRepository courseRepo,
                           UserRepository userRepo, CompetencyRepository competencyRepo) {
        this.employeeTaskDetailsRepo = employeeTaskDetailsRepo;
        this.employeeTaskHistoryRepo = employeeTaskHistoryRepo;
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
        this.competencyRepo = competencyRepo;
    }

    @Override
    public ApiReturn createdTask(EmployeeTaskReq taskReq) throws CommonException {

        Long loggedInUserId = Long.parseLong(MDC.get("userId"));

//        Course course = courseRepo.findByIdAndActive(taskReq.getCourseId(), true);
        Optional<ClientCompetency> competency = competencyRepo.findById(taskReq.getCompetencyId());
        if (!competency.isPresent()) {
            throw new CommonException("Invalid Course Details");
        }
        User employeeId = userRepo.findById(taskReq.getEmployeeId()).orElseThrow(() -> new CommonException("Invalid Employee Details"));
        List<TaskStatus> statusList = new ArrayList<TaskStatus>();
        statusList.add(TaskStatus.OPEN);
        statusList.add(TaskStatus.ACCEPETED);
        statusList.add(TaskStatus.INPROGRESS);
        statusList.add(TaskStatus.COMPLETED);
        if (!employeeTaskDetailsRepo.existsByEmployeeIdAndCompetencyAndTaskStatusIn(employeeId, competency.get(), statusList)) {
            EmployeeTaskDetails task = new EmployeeTaskDetails();
            task.setCompetency(competency.get());
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
    public List<EmployeeTaskDetails> getAllocatedTasks(Long employeeId, TaskStatus taskStatus, Long competencyId)
            throws CommonException {

        if (employeeId != null && employeeId != 0 && taskStatus != null && competencyId != null && competencyId != 0) {
            return employeeTaskDetailsRepo.findByEmployeeIdAndCompetencyAndTaskStatus(employeeId, competencyId,
                    taskStatus);
        } else if (employeeId != null && employeeId != 0 && taskStatus != null) {
            return employeeTaskDetailsRepo.findByEmployeeIdAndTaskStatus(employeeId, taskStatus);
        } else if (employeeId != null && employeeId != 0 && competencyId != null && competencyId != 0) {
            return employeeTaskDetailsRepo.findByEmployeeIdAndCompetency(employeeId, competencyId);
        } else if (taskStatus != null && competencyId != null && competencyId != 0) {
            return employeeTaskDetailsRepo.findByCompetencyAndTaskStatus(competencyId, taskStatus);
        } else if (employeeId != null && employeeId != 0) {
            return employeeTaskDetailsRepo.findByEmployeeId(employeeId);
        } else if (taskStatus != null) {
            return employeeTaskDetailsRepo.findByTaskStatus(taskStatus);
        } else if (competencyId != null && competencyId != 0) {
            return employeeTaskDetailsRepo.findByCompetency(competencyId);
        } else {
            throw new CommonException("please provide least one filter");
        }

    }

}
