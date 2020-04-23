package com.marsha.springboot.model;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "HelloWorld参数类")
public class HelloWorld {

	@ApiModelProperty(value = "要说内容")
	@NotNull(message = "不能为空或者\"\"")
	private String say;

	public String getSay() {
		return say;
	}

	public void setSay(String say) {
		this.say = say;
	}
	
}
