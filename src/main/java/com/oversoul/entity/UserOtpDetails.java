package com.oversoul.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.oversoul.enums.OtpType;

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
public class UserOtpDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long userId;

	private String otp;

	@CreationTimestamp
	private Date insertedOn;

	@UpdateTimestamp
	private Date updatedOn;

	@Enumerated(EnumType.STRING)
	private OtpType otpType;

	public UserOtpDetails(Long userId, String otp, OtpType otpType) {
		super();
		this.userId = userId;
		this.otp = otp;
		this.otpType = otpType;
	}

}
