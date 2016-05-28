package com.zheng.shiro;

import org.apache.shiro.authz.permission.WildcardPermission;
import org.junit.Test;

/**
 * 通配符权限测试
 * 
 * 资源标识符:操作:实例
 *
 * @author Administrator
 * @data 2016年5月23日 下午10:38:46
 */
public class PermissionWildcardTest extends BaseTest {
	
	@Test
	public void testWilcard() {
		login("classpath:shiro-permission-wildcard.ini", "root", "root");
//		getSubject().checkPermission("user:create:1");
//		getSubject().checkPermissions("system:user:delete", "system:user:create");
		getSubject().checkPermissions("system:dept:create,delete");
		getSubject().checkPermissions("system:dept:delete", "system:dept:create");
		
		getSubject().checkPermission("system:teacher:*");
		getSubject().checkPermissions("system:teacher");
		getSubject().checkPermissions("system:teacher:delete", "system:teacher:view");
		
		getSubject().checkPermissions("system:manager:view");
		getSubject().checkPermissions("user:view:1", "user:delete:1");
		
		getSubject().checkPermission(new WildcardPermission("system:teacher:*"));
		
	}
	
	
	
}
