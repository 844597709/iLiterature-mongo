/*package com.swust.isearchscan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.swust.isearchscan.domain.User;
import com.swust.isearchscan.utils.JsonUtil;

public class ExampleServiceTest {
	
	private ExampleService TestExampleService;
	//手动将结果放在map里
	Map<String,Object> map = new HashMap<String,Object>();
	@Before
    public void init() {
        ApplicationContext ctx = 
                new ClassPathXmlApplicationContext(
                        new String[]{"classpath:spring/applicationContext.xml",
                                     "classpath:spring/dao.xml",
                                     "classpath:spring/service.xml"});
        TestExampleService = (ExampleService)ctx.getBean("TestExampleService");
        
    }
	
	@Test()
	public void findOneUser() {
		
		map.put("aaaa", TestExampleService.findOneUser(1));
		System.out.println(JsonUtil.getJSON(map));
	}
	
	@Test()
	public void findAllUser() {
		List<User> result = TestExampleService.findAllUser();
		map.put("aaaa", result);
		System.out.println(JsonUtil.getJSON(map));
	}
}
*/