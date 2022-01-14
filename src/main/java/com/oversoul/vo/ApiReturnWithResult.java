package com.oversoul.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ApiReturnWithResult extends ApiReturn {
	private Object data = new Object();

	public ApiReturnWithResult() {
		super();
	}

	public ApiReturnWithResult(Object data) {
		super();
		this.data = data;
	}

	public ApiReturnWithResult(Integer code, String status, Object data) {
		super(code, status);
		this.data = data;
	}

}
