package com.marsha.springboot.utils;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/** -------- 通用异常处理方法 -------- **/
	@ExceptionHandler(Exception.class)
	public ResponseData error(Exception e) {
		e.printStackTrace();
		return new ResponseData().error();
	}

	/** -------- 指定异常处理方法 -------- **/
	@ExceptionHandler(NullPointerException.class)
	public ResponseData error(NullPointerException e) {
		e.printStackTrace();
		return new ResponseData().data("空指针异常");
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseData error(MethodArgumentNotValidException e) {
		return new ResponseData().code("10001").message("参数校验失败").data(e.getMessage());
	}

	/** -------- 自定义定异常处理方法 -------- **/
	@ExceptionHandler(CMSException.class)
	public ResponseData error(CMSException e) {
		e.printStackTrace();
		return new ResponseData().error().message(e.getMessage()).code(e.getCode());
	}

}
