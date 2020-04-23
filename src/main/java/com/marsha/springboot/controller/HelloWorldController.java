package com.marsha.springboot.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marsha.springboot.model.HelloWorld;
import com.marsha.springboot.service.HelloWorldService;
import com.marsha.springboot.utils.ResponseData;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/agree")
@Api(value = "类描述",tags = {"hello demo功能"})
public class HelloWorldController {

	@Autowired
	HelloWorldService helloWorldService;
	
	@ApiOperation(value = "返回say hello",notes = "方法的具体描述信息")
	@GetMapping("/helloworld")
	public ResponseData getHello(@RequestBody  @Valid HelloWorld helloWorld) {
		String say = helloWorld.getSay();
		String sayhello = helloWorldService.sayhello(say);
		return new ResponseData().ok().data(sayhello);
	}
	
}
