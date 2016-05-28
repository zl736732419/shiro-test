package com.zheng.shiro;

import org.junit.Test;

public class CustomPermissionTest extends BaseTest {

	@Test
	public void testCustomPermission() {
		login("classpath:shiro-permission-custom.ini", "root", "root");
		getSubject().checkRole("role1");
		
		getSubject().checkPermission("+user+2+10");
		getSubject().checkPermission("user2:delete");
		
		getSubject().checkPermission("+user+8");
		
	}
	
}
