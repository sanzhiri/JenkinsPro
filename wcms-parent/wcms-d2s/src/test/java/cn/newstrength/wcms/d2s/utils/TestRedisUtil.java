package cn.newstrength.wcms.d2s.utils;

import cn.newstrength.nsms.Application;
import cn.newstrength.wcms.d2s.enums.RedisQueueKey;
import cn.newstrength.wcms.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * @Auther: zrf
 * @Date: 2021/1/28 16:46
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestRedisUtil {
    @Autowired
    public RedisUtil redisUtil;
    @Test
    public void testDesc(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        int decr = 0 ;
        for (int i = 0 ; i<=5 ;i++){
            redisUtil.decr(RedisQueueKey.D2S_MESSAGE_ERROR.getKey() + uuid + "_10");
            redisUtil.decr(RedisQueueKey.D2S_MESSAGE_ERROR.getKey() + uuid + "_11");

        }
        Object o = redisUtil.get(RedisQueueKey.D2S_MESSAGE_ERROR.getKey() + uuid + "_10", 0);
        System.out.println(o);
        boolean del = redisUtil.del(RedisQueueKey.D2S_MESSAGE_ERROR.getKey() + uuid + "_10");
        System.out.println("del:"+del);
    }
    @Test
    public void testPriorityMqDel(){
        boolean b = redisUtil.zSetDel(RedisQueueKey.D2S_PRIORITY_MQ.getKey(), "54");
    }

}
