package com.marsha.springboot.service.impl;

import org.springframework.stereotype.Service;

import com.marsha.springboot.service.HelloWorldService;

@Service
public class HelloWorldServiceImpl implements HelloWorldService{

	@Override
	public String sayhello(String say) {
		return say;
	}

}
