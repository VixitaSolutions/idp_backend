package com.oversoul.vo;

import com.oversoul.enums.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTaskReq {

	private Long taskId;

	private Long employeeId;

	private Long courseId;

	private String taskName;

	private String taskDescription;

	private Long duration;

	private TaskStatus taskStatus;

}