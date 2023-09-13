package cn.newstrength.nsms;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import cn.newstrength.wcms.channel.api.dto.Channel;
import cn.newstrength.wcms.channel.api.service.ChannelService;
import cn.newstrength.wtdf.plugin.util.TranUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChannelTests {
	@Autowired
	private ChannelService channelService;

	
	public void testCount() {
		Long count = channelService.createChannelQuery().count();
		System.out.println(count);
		Assert.assertTrue(count>=0l);
	}
	
	public void testFind() {
		Channel channel = channelService.createChannelQuery().id(1L).singleResult();
		System.out.println(TranUtils.toJson(channel));
		Assert.assertTrue(channel!=null);
	}
	
	@Test
	public void testByCode() {
		List<Channel> channels = channelService.createChannelQuery().codes("11,22").list();
		System.out.println(TranUtils.toJson(channels));
		Assert.assertTrue(channels.size()>=0);
	}
}






