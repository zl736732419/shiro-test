package com.zheng.shiro;

import org.junit.Test;

public class ShiroTest extends BaseTest {
	
	@Test
	public void test() {
		
		login("classpath:shiro-atleastonesuccessful.ini", "root", "root");
		getSubject().logout();
	}
}
