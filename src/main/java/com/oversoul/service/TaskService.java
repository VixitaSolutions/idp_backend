package com.oversoul.service;

import java.util.List;

import com.oversoul.entity.EmployeeTaskDetails;
import com.oversoul.enums.TaskStatus;
import com.oversoul.exception.CommonException;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.EmployeeTaskReq;

public interface TaskService {

	ApiReturn createdTask(EmployeeTaskReq taskReq) throws CommonException;

	ApiReturn updateTask(EmployeeTaskReq taskReq) throws CommonException;

	List<EmployeeTaskDetails> getAllotedTasks(Long employeeId, TaskStatus taskStatus, Long courseType) throws CommonException;

}
