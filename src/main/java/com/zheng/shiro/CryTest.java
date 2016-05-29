package com.zheng.shiro;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.util.ByteSource;
import org.junit.Assert;
import org.junit.Test;

/**
 * 编码测试 shiro提供了可逆的编码方式：base64和hex方式，两者之间也可以合并使用
 * 采用CodecSupport提供的toString(bytes[]), toBytes(String
 * source)进行byte[]与string之间的转化
 *
 * 同时也提供了散列算法：这是不可逆的，适合用于密码加密 常用的有MD5, SHA
 *
 *
 * @author Administrator
 * @data 2016年5月26日 下午10:56:14
 */
public class CryTest extends BaseTest {

	@Test
	public void testBase64() {
		String str = "这是一个测试!";
		String base64Encoding = Base64.encodeToString(str.getBytes());
		System.out.println(base64Encoding);
		str = new String(Base64.decodeToString(base64Encoding.getBytes()));
	}

	@Test
	public void testHEx() {
		String str = "这是一个测试!";
		String hexEncoding = Hex.encodeToString(str.getBytes());
		System.out.println(hexEncoding);

		str = new String(Hex.decode(hexEncoding));
		System.out.println(str);
	}

	@Test
	public void testBase64UnionHex() {
		String str = "这是一个测试!";
		String base64Encoding = Base64.encodeToString(str.getBytes());
		String result = Hex.encodeToString(base64Encoding.getBytes());
		System.out.println(result);

		result = new String(Hex.decode(result.getBytes()));
		str = Base64.decodeToString(result);
		System.out.println(str);
	}

	@Test
	public void testMd5Hash() {
		String str = "这是一个测试!";
		String salt = "cry";

		String result = new Md5Hash(str, salt, 2).toBase64();
		System.out.println(result);
	}

	@Test
	public void testShaHashTest() {
		String str = "这是一个测试!";
		String salt = "cry";

		String result = new Sha1Hash(str, salt, 2).toBase64();
		System.out.println(result);
	}

	/**
	 * 通用的hash加密支持SimpleHash 在该类下还实现了许多算法，其中SHA1Hash和Md5Hash等都是它的子类
	 *
	 * @author Administrator
	 * @data 2016年5月26日 下午11:15:47
	 */
	@Test
	public void testSimpleHash() {
		String str = "这是一个测试!";
		String salt = "cry";

		String result = new SimpleHash("md5", str, salt, 2).toBase64();
		System.out.println(result);

		result = new SimpleHash("sha-1", str, salt, 2).toBase64();
		System.out.println(result);
	}

	@Test
	public void testDefaultHashService() {
		String str = "这是一个测试!";
		String salt = "cry";

		DefaultHashService service = new DefaultHashService();
		service.setHashAlgorithmName("md5");
		service.setPrivateSalt(ByteSource.Util.bytes(salt)); // 是否设置私盐
		service.setGeneratePublicSalt(true); // 是否生成公盐
		service.setHashIterations(2);
		service.setRandomNumberGenerator(new SecureRandomNumberGenerator()); // 用于生成公盐

		HashRequest request = new HashRequest.Builder().setAlgorithmName("md5").setIterations(2)
				.setSalt(ByteSource.Util.bytes(salt)).setSource(str).build();

		String result = service.computeHash(request).toBase64();
		System.out.println(result);

	}

	@Test
	public void testPasswordWithMyReam() {
		login("classpath:shiro-password-service.ini", "zang", "123");
		Assert.assertEquals(true, getSubject().isAuthenticated());
	}

	@Test
	public void testGeneratePassword() {
		String algorithmName = "md5";
		String username = "liu";
		String password = "123";
		String salt1 = username;
		// String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
		String salt2 = "0072273a5d87322163795118fdd7c45e";
		int hashIterations = 2;

		SimpleHash hash = new SimpleHash(algorithmName, password, salt1 + salt2, hashIterations);
		String encodedPassword = hash.toHex();
		System.out.println(salt2);
		System.out.println(encodedPassword);
	}

	@Test
	public void testJdbcPassword() {
		login("classpath:shiro-jdbc-password.ini", "wu", "123");
		Assert.assertEquals(true, getSubject().isAuthenticated());
	}

	@Test
	public void testHashedCredentialsMatcherWithMyRealm2() {
		// 使用testGeneratePassword生成的散列密码
		login("classpath:shiro-hashCredentialMatcher.ini", "liu", "123");
	}

	@Test
	public void testJdbcHashedCredentialsMatcherWithMyRealm() {
		BeanUtilsBean.getInstance().getConvertUtils().register(new EnumConverter(), JdbcRealm.SaltStyle.class);
		// 使用testGeneratePassword生成的散列密码
		login("classpath:shiro-jdbc-hashCredentialMatcher.ini", "liu", "123");
	}

	@SuppressWarnings("unused")
	private class EnumConverter extends AbstractConverter {
		@Override
		protected String convertToString(final Object value) throws Throwable {
			return ((Enum) value).name();
		}

		@Override
		protected Object convertToType(final Class type, final Object value) throws Throwable {
			return Enum.valueOf(type, value.toString());
		}

		@Override
		protected Class getDefaultType() {
			return null;
		}

	}

	@Test(expected = ExcessiveAttemptsException.class)
	public void testRetryLimitHashedCredentialsMatcherWithMyRealm() {
		for (int i = 1; i <= 5; i++) {
			System.out.println("login ... " + i);
			try {
				login("classpath:shiro-retryLimitHashedCredentialMatcher.ini", "liu", "234");
			} catch (Exception e) {}
		}
		login("classpath:shiro-retryLimitHashedCredentialMatcher.ini", "liu", "234");
	}
}
