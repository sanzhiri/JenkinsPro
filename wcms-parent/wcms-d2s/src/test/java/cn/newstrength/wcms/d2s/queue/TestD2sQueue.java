package cn.newstrength.wcms.d2s.queue;

import cn.newstrength.job.core.util.FileUtil;
import cn.newstrength.nsms.Application;
import cn.newstrength.wcms.d2s.dto.ParentD2sEvent;
import cn.newstrength.wcms.d2s.dto.channel.ChannelAddD2SEvent;
import cn.newstrength.wcms.d2s.enums.RedisQueueKey;
import cn.newstrength.wcms.d2s.message.D2sMessage;
import cn.newstrength.wtdf.plugin.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: zrf
 * @Date: 2020/12/19 19:30
 * @Description: 测试动转静消息队列
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestD2sQueue {
	public static final Logger logger = LoggerFactory.getLogger(TestD2sQueue.class);

	@Autowired
	private RedisService redisService;
	@Autowired
    D2SEventQueue d2SEventQueue;
	@Autowired
	RedisTemplate redisTemplate;
	@Autowired
	D2SFileQueue d2SFileQueue;

	@Test
	public void testD2SEventMessageProduce() {
		List<ParentD2sEvent> d2SEventMessageList = new ArrayList<>();
		ParentD2sEvent channelAddD2sEvent  = new ChannelAddD2SEvent()
				.setSiteId(1)
				.setChannelId(1)
				.getParentTransForm()
				.setLoginId("admin").build();
		d2SEventMessageList.add(channelAddD2sEvent);
		d2SEventMessageList.forEach(eventMessage -> {
			d2SEventQueue.produce(eventMessage);
		});
	}

	@Test
	public void testD2sEventMessageConsumer() {
		List<ParentD2sEvent> consumeList = d2SEventQueue.consume(20);
		consumeList.forEach(eventMessage -> {
			logger.info("{}", eventMessage.toString());
		});
	}
	@Test
	public void testD2sFileMessageProduce() throws UnsupportedEncodingException {
		for(int i= 1 ;i<=14;i++){
			String type = null;
			if(i <= 2){
				type="index";
			}else if(i > 2 && i <= 5){
				type ="channelIndex";
			}else if(i>5 && i<=7){
				type="infoDetail";
			}else if(i>7 && i<=10){
				type="channelList";
			}else{
				type="channelOther";
			}
			byte[] bytes = FileUtil.readFileContent(new File("/Users/zhangruifeng/Documents/tmp/html/index.html"));
			String content = new String(bytes, "utf-8");
			D2sMessage d2sMessage = new D2sMessage();
			d2SFileQueue.produce(d2sMessage);
		}
	}
	@Test
	public void testMonitor(){
		d2SFileQueue.monitor();
	}
	@Test
	public void testD2sFileMessageConsumer(){
		List<D2sMessage> d2sMessageList = d2SFileQueue.consume(RedisQueueKey.D2S_WAIT_CONSUME_MQ.getKey());
		if(d2sMessageList != null) {
			d2sMessageList.forEach(d2sMessage -> {
				logger.info(d2sMessage.toString());
			});
		}
	}
}
