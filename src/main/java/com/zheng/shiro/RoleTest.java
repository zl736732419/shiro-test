package com.zheng.shiro;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class RoleTest extends BaseTest {

	@Test
	public void testHasRole() {
		login("classpath:shiro-role.ini", "test", "123");
		Assert.assertEquals(true, getSubject().hasRole("role1"));
		Assert.assertEquals(true, getSubject().hasAllRoles(Lists.newArrayList("role1", "role2")));
		boolean[] result = getSubject().hasRoles(Lists.newArrayList("role1", "role2", "role3"));
		System.out.println(Arrays.toString(result));
	}
	
	@Test
	public void testCheckRole() {
		login("classpath:shiro-role.ini", "root", "root");
		getSubject().checkRole("role1");
		getSubject().checkRoles(Lists.newArrayList("role1", "role2"));
		getSubject().checkRoles("role1", "role2");
	}
	
}
