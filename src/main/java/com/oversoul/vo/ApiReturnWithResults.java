package com.oversoul.vo;

import java.util.ArrayList;
import java.util.List;

public class ApiReturnWithResults extends ApiReturn {
	private List<? extends Object> results = new ArrayList<>();
	

	public List<? extends Object> getResults() {
		return results;
	}

	public void setResults(List<? extends Object> results) {
		this.results = results;
	}

	public ApiReturnWithResults() {
		super();
	}

	public ApiReturnWithResults(List<? extends Object> results) {
		super();
		this.results = results;
	}

	public ApiReturnWithResults(Integer code, String status, String message, List<? extends Object> results) {
		super(code, status, message);
		this.results = results;
	}

}
