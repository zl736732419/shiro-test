package com.zheng.shiro;

import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.junit.Assert;
import org.junit.Test;

/**
 * 真实环境开发测试 设计到从db-dao-service一系列接口
 *
 * @author Administrator
 * @data 2016年5月29日 下午4:33:23
 */
public class RealEnvTest extends BaseTest {

	@Test
	public void testLoginSuccess() {
		login("classpath:shiro-real-env.ini", u1.getUsername(), password);
		Assert.assertTrue(getSubject().isAuthenticated());
	}

	@Test(expected = UnknownAccountException.class)
	public void testLoginFailWithUnknownUsername() {
		login("classpath:shiro-real-env.ini", u1.getUsername() + "1", password);
	}

	@Test(expected = IncorrectCredentialsException.class)
	public void testLoginFailWithErrorPassowrd() {
		login("classpath:shiro-real-env.ini", u1.getUsername(), password + "1");
	}

	@Test(expected = LockedAccountException.class)
	public void testLoginFailWithLocked() {
		login("classpath:shiro-real-env.ini", u4.getUsername(), password + "1");
	}

	@Test(expected = ExcessiveAttemptsException.class)
	public void testLoginFailWithLimitRetryCount() {
		for (int i = 1; i <= 5; i++) {
			try {
				login("classpath:shiro-real-env.ini", u3.getUsername(), password + "1");
			} catch (Exception e) {
				/* ignore */}
		}
		login("classpath:shiro-real-env.ini", u3.getUsername(), password + "1");

		// 需要清空缓存，否则后续的执行就会遇到问题(或者使用一个全新账户测试)
	}

	 @Test
	    public void testHasRole() {
	        login("classpath:shiro-real-env.ini", u1.getUsername(), password );
	        Assert.assertTrue(getSubject().hasRole("admin"));
	    }

	    @Test
	    public void testNoRole() {
	        login("classpath:shiro-real-env.ini", u2.getUsername(), password);
	        Assert.assertFalse(getSubject().hasRole("admin"));
	    }

	    @Test
	    public void testHasPermission() {
	        login("classpath:shiro-real-env.ini", u1.getUsername(), password);
	        Assert.assertTrue(getSubject().isPermittedAll("user:create", "menu:create"));
	    }

	    @Test
	    public void testNoPermission() {
	        login("classpath:shiro-real-env.ini", u2.getUsername(), password);
	        Assert.assertFalse(getSubject().isPermitted("user:create"));
	    }
	
}