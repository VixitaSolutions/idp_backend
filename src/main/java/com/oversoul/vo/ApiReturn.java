package com.oversoul.vo;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ApiReturn {
	private Integer code;
	private String status;
	private String message;

	public ApiReturn() {
		this(HttpStatus.OK.value(), "Suceess", "SUCCESS");
	}

	public ApiReturn(Integer code, String status, String message) {
		super();
		this.code = code;
		this.status = status;
		this.message = message;

	}

	public ApiReturn(Integer code, String status) {
		super();
		this.code = code;
		this.status = status;
	}

}
