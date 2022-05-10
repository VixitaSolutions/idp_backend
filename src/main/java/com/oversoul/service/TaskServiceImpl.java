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
import com.oversoul.entity.Role;
import com.oversoul.entity.User;
import com.oversoul.enums.TaskStatus;
import com.oversoul.exception.CommonException;
import com.oversoul.repository.ClientCompetencyRepository;
import com.oversoul.repository.CourseRepository;
import com.oversoul.repository.EmployeeTaskDetailsRepository;
import com.oversoul.repository.EmployeeTaskHistoryRepository;
import com.oversoul.repository.RoleRepository;
import com.oversoul.repository.UserMappingRepository;
import com.oversoul.repository.UserRepository;
import com.oversoul.repository.UserRoleRepository;
import com.oversoul.util.ApiConstants;
import com.oversoul.util.Constants;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.EmployeeTaskReq;

@Service
public class TaskServiceImpl implements TaskService {

    private final EmployeeTaskDetailsRepository employeeTaskDetailsRepo;

    private final EmployeeTaskHistoryRepository employeeTaskHistoryRepo;

    private final CourseRepository courseRepo;

    private final ClientCompetencyRepository clientCompetencyRepo;

    private final UserRepository userRepo;

    private final UserMappingRepository userMappingRepo;

    private final UserRoleRepository userRoleRepo;

    private final RoleRepository roleRepo;

    public TaskServiceImpl(EmployeeTaskDetailsRepository employeeTaskDetailsRepo,
                           EmployeeTaskHistoryRepository employeeTaskHistoryRepo, CourseRepository courseRepo,
                           UserRepository userRepo, ClientCompetencyRepository clientCompetencyRepo, UserMappingRepository userMappingRepo, UserRoleRepository userRoleRepo, RoleRepository roleRepo) {
        this.employeeTaskDetailsRepo = employeeTaskDetailsRepo;
        this.employeeTaskHistoryRepo = employeeTaskHistoryRepo;
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
        this.clientCompetencyRepo = clientCompetencyRepo;
        this.userMappingRepo = userMappingRepo;
        this.userRoleRepo = userRoleRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public ApiReturn createdTask(EmployeeTaskReq taskReq) throws CommonException {

        Long loggedInUserId = Long.parseLong(MDC.get("userId"));

//        Course course = courseRepo.findByIdAndActive(taskReq.getCourseId(), true);
        Optional<ClientCompetency> competency = clientCompetencyRepo.findById(taskReq.getCompetencyId());
        if (!competency.isPresent()) {
            throw new CommonException("Invalid Course Details");
        }
        User employee = userRepo.findById(taskReq.getEmployeeId()).orElseThrow(() -> new CommonException("Invalid Employee Details"));
        List<TaskStatus> statusList = new ArrayList<TaskStatus>();
        statusList.add(TaskStatus.OPEN);
        statusList.add(TaskStatus.ACCEPTED);
        statusList.add(TaskStatus.IN_PROGRESS);
        statusList.add(TaskStatus.COMPLETED);
        if (!employeeTaskDetailsRepo.existsByEmployee_IdAndCompetencyAndTaskStatusIn(employee.getId(), competency.get(), statusList)) {
            EmployeeTaskDetails task = new EmployeeTaskDetails();
            task.setCompetency(competency.get());
            task.setCreatedBy(loggedInUserId);
            task.setDuration(taskReq.getDuration());
            task.setEmployee(employee);
            task.setTaskDescription(taskReq.getTaskDescription());
            task.setTaskName(taskReq.getTaskName());
            task.setTaskStatus(TaskStatus.OPEN);
            task.setProgress(0l);
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
        taskHistory.setEmpcomments(taskReq.getEmpComments());
        taskHistory.setCoachComments(taskReq.getCoachComments());
        taskHistory.setTaskDescription(taskReq.getTaskDescription());
        taskHistory.setCommentedBy(loggedInUserId);
        taskHistory.setEmployeeTaskId(task.getId());
        taskHistory.setStatus(taskReq.getTaskStatus());
        employeeTaskHistoryRepo.save(taskHistory);
    }

    @Override
    public ApiReturn updateTask(EmployeeTaskReq taskReq) throws CommonException {
        EmployeeTaskDetails task = employeeTaskDetailsRepo.findById(taskReq.getId()).orElseThrow(() -> new CommonException("Task Not existed"));
        Long loggedInUserId = Long.parseLong(MDC.get("userId"));
        task.setTaskStatus(taskReq.getTaskStatus());
        task.setProgress(taskReq.getProgress());
        if (taskReq.getTaskStatus() == TaskStatus.REJECTED)
        	task.setEmpComments(taskReq.getEmpComments());
        if (taskReq.getTaskStatus() == TaskStatus.DECLINED)
        	task.setCoachComments(taskReq.getCoachComments());
        
        employeeTaskDetailsRepo.save(task);
        createTaskHistory(taskReq, loggedInUserId, task);
        return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                "Task updated Successfully");
    }

    @Override
    public List<EmployeeTaskDetails> getAllocatedTasks(Long employeeId, TaskStatus taskStatus, Long competencyId)
            throws CommonException {

        if (employeeId != null && employeeId != 0 && taskStatus != null && competencyId != null && competencyId != 0) {
            return employeeTaskDetailsRepo.findByEmployee_IdAndCompetencyAndTaskStatus(employeeId, competencyId,
                    taskStatus);
        } else if (employeeId != null && employeeId != 0 && taskStatus != null) {
            return employeeTaskDetailsRepo.findByEmployee_IdAndTaskStatus(employeeId, taskStatus);
        } else if (employeeId != null && employeeId != 0 && competencyId != null && competencyId != 0) {
            return employeeTaskDetailsRepo.findByEmployee_IdAndCompetency(employeeId, competencyId);
        } else if (taskStatus != null && competencyId != null && competencyId != 0) {
            return employeeTaskDetailsRepo.findByCompetencyAndTaskStatus(competencyId, taskStatus);
        } else if (employeeId != null && employeeId != 0) {
            return employeeTaskDetailsRepo.findByEmployee_Id(employeeId);
        } else if (taskStatus != null) {
            return employeeTaskDetailsRepo.findByTaskStatus(taskStatus);
        } else if (competencyId != null && competencyId != 0) {
            return employeeTaskDetailsRepo.findByCompetency(competencyId);
        } else {
            throw new CommonException("please provide least one filter");
        }

    }

    @Override
    public List<EmployeeTaskDetails> getAllocatedTasksByCoachId(Long coachId, TaskStatus taskStatus, Long competencyId) throws CommonException {

        Role role = roleRepo.findById(Long.valueOf(Constants.COACH)).orElseThrow(() -> new CommonException("Role Not Found"));
        if (userRoleRepo.existsByUserIdAndRoleId(coachId, role)) {
            List<Long> employeeIds = userMappingRepo.findEmployeeIdByCoachId(coachId);
            Optional.ofNullable(employeeIds).orElseThrow(() -> new CommonException("Employees Not Found"));
            if (taskStatus != null && competencyId != null && competencyId != 0) {
                return employeeTaskDetailsRepo.findByEmployee_IdInAndCompetencyAndTaskStatus(employeeIds, competencyId,
                        taskStatus);
            } else if (taskStatus != null) {
                return employeeTaskDetailsRepo.findByEmployee_IdInAndTaskStatus(employeeIds, taskStatus);
            } else if (competencyId != null && competencyId != 0) {
                return employeeTaskDetailsRepo.findByEmployee_IdInAndCompetency(employeeIds, competencyId);
            } else {
                return employeeTaskDetailsRepo.findByEmployee_IdIn(employeeIds);
            }
        }
        throw new CommonException("User Not Found with given Details");
    }

}
