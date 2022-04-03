package com.oversoul.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
@Entity
public class EmployeeTaskHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long employeeTaskId;

	private TaskStatus status;

	private Date createdOn;
	
	@Column(length = 500)
	private String taskDescription;

	@Column(length = 500)
	private String empcomments;
	
	@Column(length = 500)
	private String coachComments;

	private Long commentedBy;

}
