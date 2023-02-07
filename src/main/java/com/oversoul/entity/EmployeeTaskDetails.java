package com.oversoul.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
public class EmployeeTaskDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private User employee;

	@ManyToOne
	private ClientCompetency competency;

	private String taskName;

	private Long duration;

	@Column(length = 500)
	private String taskDescription;

	private String referanceUrl;

	private Long createdBy;

	@CreationTimestamp
	private Date createdOn;

	@UpdateTimestamp
	private Date updatedOn;

	@Enumerated(EnumType.STRING)
	private TaskStatus taskStatus;
	
	@Column(length = 500)
	private String empComments;
	
	@Column(length = 500)
	private String coachComments;
	
	private Long progress;

}
