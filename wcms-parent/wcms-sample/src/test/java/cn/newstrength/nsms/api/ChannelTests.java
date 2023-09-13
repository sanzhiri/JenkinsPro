package cn.newstrength.nsms.api;

import cn.newstrength.wcms.channel.api.dto.BreadCrumb;
import cn.newstrength.wcms.channel.api.dto.Channel;
import cn.newstrength.wcms.channel.api.service.ChannelService;
import cn.newstrength.wtdf.plugin.util.TranUtils;
import io.jsonwebtoken.lang.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChannelTests {
	private final static Logger logger = LoggerFactory.getLogger(ChannelTests.class);
	
	@Autowired
	private ChannelService channelService;

	@Test
	public void queryChannelLink(){
		Long channelId = 336L;
		logger.info("根据栏目id查询栏目链接地址");
		Channel channel = channelService.createChannelQuery().id(channelId).singleResult();
		Assert.notNull(channel);
		logger.info("首页链接{}",channel.getHomeUrl());
		logger.info("列表链接{}",channel.getListUrl());
		logger.info("外部链接 ? {} , 链接 {}",channel.isOutLink(),channel.getOutLinkUrl());
		logger.info("内部链接 ? {} , 链接 {}",channel.isInnerLink(),channel.getInnerLinkUrl());
	}

	@Test
	public void queryChannel() {
		Channel channel = channelService.createChannelQuery().id(220L).singleResult();
		logger.info("Channel : {}",TranUtils.toJson(channel));
		
	}

	@Test
	public void listBySiteIdAndLevel(){
		Long siteId = 1L;
		int level = 2;
		logger.info("根据站点id+密级查询栏目列表");
		List<Channel> channels = channelService.createChannelQuery().siteId(siteId).securityLevel(level).list();
		this.loopList(siteId,channels);
	}

	@Test
	public void listSetCode(){
		Long siteId = 330L;
		logger.info("根据站点id+指定栏目code查询栏目列表");
		List<Channel> channels = channelService.createChannelQuery().siteId(siteId).codes("professional").list();
		this.loopList(siteId,channels);
	}

	@Test
	public void listBreadCrumbById(){
		Long id = 337L;
		List<BreadCrumb> breadCrumbs = channelService.createChannelQuery().id(id).breadCrumb();
		this.loopBreadCrumb(breadCrumbs);
	}

	@Test
	public void listBreadCrumbByCode(){
		String channelCode = "banks";
		String siteCode = "newstrength";
		List<BreadCrumb> breadCrumbs = channelService.createChannelQuery().siteCode(siteCode).singleCode(channelCode).breadCrumb();
		this.loopBreadCrumb(breadCrumbs);
	}

	private void loopBreadCrumb(List<BreadCrumb> breadCrumbs){
		logger.info("栏目数量 {} 个",breadCrumbs.size());
		breadCrumbs.forEach(item -> {
			logger.info("栏目'{}'的 HomeUrl：{} , ListUrl：{}",item.getName(),item.getHomeUrl(),item.getListUrl());
		});
	}

	private void loopList(Long siteId,List<Channel> channels){
		logger.info("站点: {} 下有栏目数量 {} 个",siteId,channels.size());
		channels.forEach(item -> {
			logger.info("栏目'{}'的 HomeUrl：{} , ListUrl：{}",item.getName(),item.getHomeUrl(),item.getListUrl());
		});
	}
}





