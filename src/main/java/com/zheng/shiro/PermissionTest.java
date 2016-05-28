package com.zheng.shiro;

import org.junit.Assert;
import org.junit.Test;


public class PermissionTest extends BaseTest {
	@Test
	public void testCheckPermission() {
		login("classpath:shiro-permission.ini", "root", "root");
		getSubject().checkPermission("user:create");
		getSubject().checkPermissions("user:view", "user:update", "user:delete");
	}
	
	@Test
	public void testIsPermission() {
		login("classpath:shiro-permission.ini", "root", "root");
		Assert.assertEquals(true, getSubject().isPermitted("user:delete"));
		Assert.assertEquals(true, getSubject().isPermittedAll("user:view", "user:delete", "user:create"));
	}
	
}
