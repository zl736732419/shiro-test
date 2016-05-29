package com.zheng.shiro;


import java.util.Collection;
import java.util.Set;

import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

import com.zheng.domain.User;

public class MultiRealmTest extends BaseTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		login("classpath:shiro-multi-realm.ini", "root", "root");
		Subject subject = getSubject();
		
		Object principal = subject.getPrincipal();
		System.out.println(principal);
		PrincipalCollection princialCollection = subject.getPrincipals();
		Set<Object> set = princialCollection.asSet();
		System.out.println(set);
		Object principal2 = princialCollection.getPrimaryPrincipal();
		System.out.println(principal2);
		
		//这里返回的结果是不确定的
		Assert.assertEquals(principal, principal2);
		Set<String> realmNames = princialCollection.getRealmNames();
		System.out.println(realmNames);
		
		 Collection<User> users = princialCollection.fromRealm("myRealm3");
	     System.out.println(users);
		
	}
	
}
