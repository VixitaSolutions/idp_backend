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
	// private List<String> errors = new ArrayList<String>(0);
	// private String requestId = MDC.get("requestId");
	// private String timestamp =
	// Instant.now().atZone(ZoneId.systemDefault()).toString();

	/*
	 * private List list; private Object obj;
	 */
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
