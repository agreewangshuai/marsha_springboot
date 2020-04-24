# springboot-单元测试梳理

> 今天主要是梳理springboot单元测试，基于MockMvc进行实战演示。

[TOC]

## 1. sringboot的测试类库

springboot本身就集成并且提供了非常多的实用工具以及注解。帮助开发者更加专注业务开发。

在这里主要介绍一下，springboot提供的测试方面的工具以及注解。主要包括两个模块：

- spring-boot-test： 支持测试的核心内容。

- spring-boot-test-autoconfigure：支持测试的自动化配置

我们只需要引入上面的依赖，springboot就会帮我们引入测试模块。我们只需要使用注解。就能轻松完成调用。

比如引入的测试类库有：

- Junit: java单元测试的标准类库。
- Spring Test & Spring Boot Test: Springboot功能集成化测试支持。
- AssertJ: 一个轻量级的断言类库
- Hamcrest: 一个对象匹配器类库
- Mockito: 一个java Mock测试框架

## 2. Junit 4常用注解

- ==@BeforeClass:== 针对所有的测试，只执行一次，且方法还必须是static void

- ==@Before:==初始化方法，执行当前测试类的每个测试方法前执行

- ==@Test:==测试方法，在这里执行测试，也可以测试期望异常和超时时间(可以查看注解源码，查看使用属性)

- ==@After:==释放资源，可以执行当前测试类的每一个测试方法后执行

- ==@AfterClass:== 针对所有的测试，只执行一次，且方法还必须是static void

- ==@Ignore:== 忽略此测试方法

- ==@Runwith:== 可以更改测试运行器 

  **一个单元测试类执行的顺序：**

  `@BeforeClass` -> `@Before` -> `@Test` -> `@After` -> `@AfterClass`

  **每个一个测试方法执行的调用顺序：**

  `@Before` -> `@Test` -> `@After`

## 3. @RunWith  @SpringBootTest

### 3.1 @RunWith

在JUnit中有很多个Runner，他们负责调用你的测试代码，每一个Runner都有各自的特殊功能，你要根据需要选择不同的Runner来运行你的测试代码。在测试的时候，不指定使用的是默认的Runner。只能测试普通的java测试。不涉及`spring web`项目

在springboot中，SpringBoot 2.X 默认使用Junit4 我们使用注解`@RunWith(SpringRunner.class)`进行指定

```java
public final class SpringRunner extends SpringJUnit4ClassRunner {
	public SpringRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}
}
```

可以看到源码中，`SpringRunner`继承的`SpringJUnit4ClassRunner`

###  3.2 @SpringBootTest

@SpringBootTest注解是SpringBoot自1.4.0版本开始引入的一个用于测试的注解。他是属于SpringBoot的注解。

在进行web 环境测试的时候，我通常使用`@SpringBootTest(classes = SpringbootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)`

其中==classes = SpringbootApplication.class==是指定启动类、==webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT==是指定web环境

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(SpringBootTestContextBootstrapper.class)
public @interface SpringBootTest {
	@AliasFor("properties")
	String[] value() default {};
	@AliasFor("value")
	String[] properties() default {};
	Class<?>[] classes() default {};
	WebEnvironment webEnvironment() default WebEnvironment.MOCK;
	enum WebEnvironment {
		MOCK(false),
		RANDOM_PORT(true),
		DEFINED_PORT(true),
		NONE(false);
		private final boolean embedded;
		WebEnvironment(boolean embedded) {
			this.embedded = embedded;
		}
		public boolean isEmbedded() {
			return this.embedded;
		}
	}
}
```

查看源码发现：

`SpringBootTest.WebEnvironment `中一共有 4 个枚举值

- **==MOCK:==**     MOCK 是用的比较多的一个属性。它会判断，当你使用 servlet API 时，则使用模拟 servlet 环境创建 WebApplicationContext；如果是使用 WebFlux，则使用 ReactiveWebApplicationContext；否则，使用常规的 ApplicationContext。
- **==RANDOM_PORT:==**  创建一个 web 应用程序上下文，可以是 reactive，也可以是 servlet。它将随机起一个端口并监听。到底是 reactive 还是 Servlet，它会自己根据上下文来判断
- **==DEFINED_PORT:==** 创建一个（reactive） web 应用程序上下文，使用默认端口。针对这个属性，网上翻译的解读比较多，错误的也多。
- ==**NONE: **==   它不会指定 SpringApplication.setWebApplicationType。以非 Web 环境来运行。也就是非 Servlet 和 Reactive 环境

还有不少人的文章说，SpringBootTest.WebEnvironment.RANDOM_PORT 必须或通常和 @LocalServerPort 一起使用。我看源码的注释，解释并没有说跟@LocalServerPort 一起使用。我本身也不用。

为了减少篇幅，文中源码注释我都做了删除处理。感兴趣可以自己去查看。

## 4. MockMVC介绍

MockMvc是由spring-test包提供，实现了对Http请求的模拟，能够直接使用网络的形势，转换到Controller的调用，是的测试速度快，不依赖网络环境。同时提供了一套验证的工具，结果的验证十分方便。
基于RESTful 风格的springMVC测试。可以测试整个SpringMVC流程。从URL请求到Controller控制层的测试。

### 4.1 MockMvc

- ==作用：==服务器端SpringMVC测试的主入口点
- ==创建：== 通过`MockMVCBuilders`的静态方法建造`MockMVCBuilder`,`MockMvc`由`MockMVCBuilder`构造
- ==调用：== **perform**(RequestBuilder rb)，执行一个RequestBuilder请求，会自动执行SpringMVC的流程并映射到相应的控制器执行处理，该方法的返回值是一个ResultActions。

### 4.2 MockMVCBuilders

- ==作用：==负责创建MockMVCBuilder对象
- ==创建：== 有两种创建方式
  - **standaloneSetup**(Object... controllers): 通过参数指定一组控制器，这样就不需要从上下文获取了。
  - **webAppContextSetup**(WebApplicationContext wac)：指定WebApplicationContext，将会从该上下文获取相应的控制器并得到相应的MockMv

### 4.3 MockMVCBuilder

- ==作用：== **MockMVCBuilder**使用构造者模式来构建MockMvc的构造器
- ==创建：== 主要的实现：**StandaloneMockMvcBuilder**和**DefaultMockMvcBuilder**
- 也可以直接使用**静态工厂MockMvcBuilders创建**即可，不需要直接使用上面两个实现类

###  4.4  MockMvcRequestBuilders

- ==作用：==用来构建Request请求的。
- ==创建：== 其主要有两个子类**MockHttpServletRequestBuilder**和**MockMultipartHttpServletRequestBuilder**（如文件上传使用），即用来Mock客户端请求需要的所有数据。

### 4.5  ResultActions

方法介绍：

- **andExpect**：添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确。
- **andDo**：添加ResultHandler结果处理器，比如调试时打印结果到控制台。
- **andReturn**：最后返回相应的**MvcResult**；然后进行自定义验证/进行下一步的异步处理。
- **MockMvcResultMatchers**
  - 用来匹配执行完请求后的**结果验证。
  - 果匹配失败将抛出相应的异常。
  - 包含了很多验证API方法。
- **MockMvcResultHandlers**
  - 结果处理器，表示要对结果做点什么事情。
  - 比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息。

### 4.6 MvcResult

- ==作用：== 单元测试执行结果，可以针对执行结果进行**自定义验证逻辑**。采用断言验证数据。

## 5. 实战

完整的代码github路径： `git@github.com:agreewangshuai/marsha_springboot.git`（一个完整的springboot骨架集成统一结果返回、统一异常处理、统一日志框架以及单元测试、swagger）

**添加Gradle依赖**

```groovy
testImplementation('org.springframework.boot:spring-boot-starter-test')
```

**model**

```java
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
```

**controller**

```java
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
```

**service**

```java
public interface HelloWorldService {
	/**
	 * @param say
	 * @return
	 */
   String sayhello(String say);
}

```

**serviceImpl**

```java
@Service
public class HelloWorldServiceImpl implements HelloWorldService{
	@Override
	public String sayhello(String say) {
		return say;
	}
}
```

**Test**

```java
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
        /**
        * 添加请求参数，此处示例传参格式为json
        * new JsonHelper().objectToJson(helloWorld)是我自己写的一个util类，将model转为json
        * 嫌麻烦也可以直接写成json格式：
        *  content("{\"say\":\"hello world\"}")
        */
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
```

## 小结：

本文单元测试主要描述了springboot单元测试的MockMvc。主要针对controller层的单元测试方法的介绍。目前暂时用的功能就这么多。后续有新的研究心得，会持续更新。如有纰漏，欢迎指正。谢谢

