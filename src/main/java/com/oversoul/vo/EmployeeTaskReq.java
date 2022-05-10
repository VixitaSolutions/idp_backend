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

	private Long id;

	private Long employeeId;

	private Long competencyId;

	private String taskName;

	private String taskDescription;

	private Long duration;

	private TaskStatus taskStatus;
	
	private String empComments;
	
	private String coachComments;
	
	private Long progress;

}
