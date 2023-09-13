package cn.newstrength.nsms;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.newstrength.wtdf.plugin.redis.RedisKey;
import cn.newstrength.wtdf.plugin.service.RedisService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SQLInjectionAttackTests {
	@Autowired
	private RedisService redisService;

	@Test
	public void config() {
		String sqls = "AND|EXEC|INSERT|SELECT|DELETE|UPDATE|COUNT|CHR|MID|MASTER|TRUNCATE|CHAR|DECLARE";
		redisService.put(RedisKey.SQL_INJECTION.getKey(), sqls);
		Object value = redisService.get(RedisKey.SQL_INJECTION.getKey());
		Assert.assertNotNull(value);
	}
}
