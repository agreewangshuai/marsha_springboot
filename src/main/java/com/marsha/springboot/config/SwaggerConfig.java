package com.marsha.springboot.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
    @Bean
    public  Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("new group")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.agree.springboot.controller"))
                .build();
    }

    
    public static final Contact CONTACT = new Contact(
            "acaas",
            "agree.com.cn",
            "swagger@agree.com.cn");
    
    @SuppressWarnings("rawtypes")
	public ApiInfo apiInfo(){
        return new ApiInfo(
                "Swagger API文档",
                "关于文档描述",
                "1.0",
                "urn:tos",
                CONTACT,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<VendorExtension>());
    }
}
