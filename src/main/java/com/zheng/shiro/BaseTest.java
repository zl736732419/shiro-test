package com.zheng.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Before;

import com.zheng.domain.Permission;
import com.zheng.domain.Role;
import com.zheng.domain.User;
import com.zheng.service.PermissionService;
import com.zheng.service.RoleService;
import com.zheng.service.UserService;
import com.zheng.service.impl.PermissionServiceImpl;
import com.zheng.service.impl.RoleServiceImpl;
import com.zheng.service.impl.UserServiceImpl;
import com.zheng.utils.JdbcTemplateUtils;

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
	
	protected PermissionService permissionService = new PermissionServiceImpl();
    protected RoleService roleService = new RoleServiceImpl();
    protected UserService userService = new UserServiceImpl();

    protected String password = "123";

    protected Permission p1;
    protected Permission p2;
    protected Permission p3;
    protected Role r1;
    protected Role r2;
    protected User u1;
    protected User u2;
    protected User u3;
    protected User u4;

//    @Before
    public void setUp() {
        JdbcTemplateUtils.getJdbcTemplate().update("delete from sys_users");
        JdbcTemplateUtils.getJdbcTemplate().update("delete from sys_roles");
        JdbcTemplateUtils.getJdbcTemplate().update("delete from sys_permissions");
        JdbcTemplateUtils.getJdbcTemplate().update("delete from sys_users_roles");
        JdbcTemplateUtils.getJdbcTemplate().update("delete from sys_roles_permissions");


        //1、新增权限
        p1 = new Permission("user:create", "用户模块新增", Boolean.TRUE);
        p2 = new Permission("user:update", "用户模块修改", Boolean.TRUE);
        p3 = new Permission("menu:create", "菜单模块新增", Boolean.TRUE);
        permissionService.createPermission(p1);
        permissionService.createPermission(p2);
        permissionService.createPermission(p3);
        //2、新增角色
        r1 = new Role("admin", "管理员", Boolean.TRUE);
        r2 = new Role("user", "用户管理员", Boolean.TRUE);
        roleService.createRole(r1);
        roleService.createRole(r2);
        //3、关联角色-权限
        roleService.correlationPermission(r1.getId(), p1.getId());
        roleService.correlationPermission(r1.getId(), p2.getId());
        roleService.correlationPermission(r1.getId(), p3.getId());

        roleService.correlationPermission(r2.getId(), p1.getId());
        roleService.correlationPermission(r2.getId(), p2.getId());

        //4、新增用户
        u1 = new User("zhang", password);
        u2 = new User("li", password);
        u3 = new User("wu", password);
        u4 = new User("wang", password);
        u4.setLocked(Boolean.TRUE);
        userService.createUser(u1);
        userService.createUser(u2);
        userService.createUser(u3);
        userService.createUser(u4);
        //5、关联用户-角色
        userService.correlationRole(u1.getId(), r1.getId());

    }
}
