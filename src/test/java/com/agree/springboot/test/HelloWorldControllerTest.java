package com.agree.springboot.test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.marsha.springboot.SpringbootApplication;
import com.marsha.springboot.model.HelloWorld;
import com.marsha.springboot.utils.JsonHelper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloWorldControllerTest {

	public static final Logger log = LoggerFactory.getLogger(HelloWorldControllerTest.class);

	@Autowired
	private WebApplicationContext ctx;
	private MockMvc mvc;

	@Before
	public void before() {
		mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}

	@Test
	public void getHelloTest() throws Exception {
		RequestBuilder request = null;
		MvcResult mvcResult = null;
		HelloWorld helloWorld = new HelloWorld();
		// helloWorld.setSay("say agree");
		request = MockMvcRequestBuilders.get("/agree/helloworld").content(new JsonHelper().objectToJson(helloWorld))
				.contentType(MediaType.APPLICATION_JSON);
		mvcResult = mvc.perform(request).andExpect(status().isOk()).andReturn();
		// 获取数据
		JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
		log.info("输出返回结果:{}", jsonObject);
		// 加断言，判断属性值的问题。
		Assert.assertNotNull(jsonObject.get("code"));
		Assert.assertEquals(jsonObject.get("code"), "0");
		Assert.assertNotNull(jsonObject.get("message"));
		Assert.assertEquals(jsonObject.get("message"), "操作成功");
		Assert.assertNotNull(jsonObject.get("data"));
	}

}
