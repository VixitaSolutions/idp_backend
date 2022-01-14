package com.oversoul.util;

public class ApiConstants {

	public enum Status {
		SUCCESS, FAILED
	}

	public enum Messages {

		SUCCESS("");

		private final String message;

		private Messages(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

	}

	public enum CustomCodes {

		SUCCESS(202, "SUCCESS");

		private final Integer code;
		private final String value;

		private CustomCodes(Integer code, String value) {
			this.code = code;
			this.value = value;

		}

		public Integer getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}

	}

}
