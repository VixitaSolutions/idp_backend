package com.oversoul.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
public class UserMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long managerId;

	private Long coachId;

	private Long employeeId;

	@CreationTimestamp
	private Date createdOn;

	@UpdateTimestamp
	private Date updatedOn;

	private Long mappedBy;

	private boolean active = true;

	private Long deLinked;

	private Long deLinkedBy;

	private Date deLinkedOn;

	public UserMapping(Long managerId, Long coachId, Long employeeId, Long mappedBy) {
		super();
		this.managerId = managerId;
		this.coachId = coachId;
		this.employeeId = employeeId;
		this.mappedBy = mappedBy;
	}

}
