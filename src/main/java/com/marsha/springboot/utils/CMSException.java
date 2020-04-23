package com.marsha.springboot.utils;

@SuppressWarnings("serial")
public class CMSException extends RuntimeException {
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public CMSException(String code, String message) {
		super(message);
		this.code = code;
	}

	@Override
	public String toString() {
		return "CMSException{" + "code=" + code + ", message=" + this.getMessage() + '}';
	}
}