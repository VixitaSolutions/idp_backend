package com.oversoul.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String email;

	private String firstName;

	private String lastName;

	private String userName;

	private String password;

	private Long lastLoginTime;

	private Boolean signUpDone;

	private Date signUpDoneOn;

	@CreationTimestamp
	private Date createdOn;

	@UpdateTimestamp
	private Date updatedOn;

	private Long createdBy;

	@Size(min = 10, max = 10)
	private String mobile;

	@Column(length = 50)
	@Type(type = "uuid-char")
	private UUID tenantId;

	private boolean fromFileUpload;

	private boolean active=true;

}
