package com.zheng.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Assert;

public class BaseTest {

	protected void login(String config, String username, String password) {
		//创建securitymanagerfactory
		IniSecurityManagerFactory factory = new IniSecurityManagerFactory(config);
		//获取shiro心脏securityManager
		org.apache.shiro.mgt.SecurityManager manager = factory.createInstance();
		
		SecurityUtils.setSecurityManager(manager);
		
		//获取用户调用门面subject,这个是全局的
		Subject subject = getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		try {
			subject.login(token);
		} catch (AuthenticationException e) {
			/*
			 * 异常情况：
			 * DisableAccountException
			 * LockedAccountException
			 * UnkownAccountException
			 * ExpiredCredentialException
			 * IncorrectCredentialException
			 * ExtensiveAttemptsException
			 */
			e.printStackTrace();
		}
		
//		Assert.assertEquals(true, subject.isAuthenticated());
		
	}
	
	public Subject getSubject() {
		return SecurityUtils.getSubject();
	}
	
	@After
	public void after() {
		ThreadContext.unbindSecurityManager();
	}
}
