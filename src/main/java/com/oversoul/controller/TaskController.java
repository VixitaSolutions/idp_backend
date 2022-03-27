package com.oversoul.controller;

import com.oversoul.enums.TaskStatus;
import com.oversoul.exception.CommonException;
import com.oversoul.service.TaskService;
import com.oversoul.util.ApiConstants;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.ApiReturnWithResult;
import com.oversoul.vo.EmployeeTaskReq;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task/")
@SecurityRequirement(name = "Authorization")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("create")
    public ApiReturn createdTask(@RequestBody EmployeeTaskReq taskReq) throws CommonException {
        return taskService.createdTask(taskReq);
    }

    @PostMapping("update")
    public ApiReturn updateTask(@RequestBody EmployeeTaskReq taskReq) throws CommonException {
        return taskService.updateTask(taskReq);
    }

    @GetMapping("getAllocatedTask")
    public ApiReturn getAllocatedTasks(@RequestParam(required = false) Long employeeId,
                                       @RequestParam(required = false) TaskStatus taskStatus, @RequestParam(required = false) Long courseType) throws CommonException {

        return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                taskService.getAllocatedTasks(employeeId, taskStatus, courseType));
    }

    @GetMapping("getAllocatedTask/byCoachId")
    public ApiReturn getAllocatedTasksByCoachId(@RequestParam(required = true) Long coachId,
                                                @RequestParam(required = false) TaskStatus taskStatus, @RequestParam(required = false) Long courseType) throws CommonException {

        return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                taskService.getAllocatedTasksByCoachId(coachId, taskStatus, courseType));
    }

}
