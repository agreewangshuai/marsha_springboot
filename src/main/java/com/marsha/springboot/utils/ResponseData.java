package com.marsha.springboot.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "返回结果类")
public class ResponseData{

	@ApiModelProperty(value = "返回状态码")
	private String code;

	@ApiModelProperty(value = "返回状态信息")
	private String message;

	@ApiModelProperty(value = "返回实体")
	private Object data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	// 通用返回成功
	public  ResponseData ok() {
		ResponseData responseData = new ResponseData();
		responseData.setCode(ResultCode.SUCCESS);
		responseData.setMessage(ResultCode.SUCCESS_OPERATE);
		return responseData;
	}

	//通用返回失败，未知错误
	public  ResponseData error() {
		ResponseData responseData = new ResponseData();
		responseData.setCode(ResultCode.FAIL);
		responseData.setMessage(ResultCode.FAIL_OPERATE);
		return responseData;
	}

	/** ------------使用链式编程，返回类本身----------- **/
	// 自定义返回数据
	public ResponseData data(Object data) {
		this.setData(data);
		return this;
	}
	// 自定义状态信息
	public ResponseData message(String message) {
		this.setMessage(message);
		return this;
	}
	// 自定义状态码
	public ResponseData code(String code) {
		this.setCode(code);
		return this;
	}
}
