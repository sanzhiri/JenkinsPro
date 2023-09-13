package cn.newstrength.nsms.api;

import cn.newstrength.wcms.info.api.dto.Info;
import cn.newstrength.wcms.info.api.query.InfoQuery;
import cn.newstrength.wcms.info.api.service.InfoService;
import cn.newstrength.wcms.util.ApiParams;
import cn.newstrength.wtdf.plugin.result.PageableResult;
import cn.newstrength.wtdf.plugin.util.TranUtils;
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
public class InfoTests {
	private final static Logger logger = LoggerFactory.getLogger(InfoTests.class);
	
	@Autowired
	private InfoService infoService;
	
	@Test
	public void queryById() {
		Info info = infoService.createInfoQuery().channelId(196L).infoId(145L).singleResult();
		logger.info("信息 : {}",TranUtils.toJson(info));
	}

	@Test
	public void queryByIdSetLevel(){
		int securityLevel = 1;
		long infoId = 13L;
		long channelId = 1L;
		Info info = infoService.createInfoQuery().channelId(channelId).infoId(infoId).securityLevel(securityLevel).singleResult();
		logger.info("信息 {} 密级 {} 的内容: {}",infoId,securityLevel,TranUtils.toJson(info));
	}

	@Test
	public void queryCount(){
		long count = infoService.createInfoQuery().channelId(1L).count();
		logger.info("栏目：{} 下信息总数量为:{}",1L,count);
	}

	@Test
	public void queryCreateTime(){
		Long infoId = 15L;
		String createTime = infoService.createInfoQuery().infoId(infoId).queryCreateTime();
		logger.info("信息:{}的创建时间是:{}",infoId,createTime);
	}

	@Test
	public void idQuery(){
		Long channelId = 1L;
		List<Long> ids = infoService.createInfoQuery().channelId(channelId).idQuery();
		logger.info("栏目：{} 下的 ids 共 {} 个",channelId,ids.size());
	}

	@Test
	public void listPage(){
		Long siteId= 2L;
		String channelCode = "job";
		PageableResult<List<Info>> listPage = infoService.createInfoQuery().siteId(siteId).channelCode(channelCode).listPage(1, 10);
		logger.info("栏目：{} 下的 信息 共 {} 条",channelCode,listPage.getData().size());
	}

	@Test
	public void list(){
		Long channelId = 1L;
		//List<Info> list = infoService.createInfoQuery().channelId(channelId).startTime("2020-12-27 10:00:21").endTime("2020-12-27 15:01:21").list();
		List<Info> list = infoService.createInfoQuery().siteId(1L).securityLevel(3).list();
		logger.info("栏目：{} 下的 信息为 {} ",channelId,TranUtils.toJson(list));
	}

	@Test
	public void clean(){
		InfoQuery infoQuery = infoService.createInfoQuery();
		infoQuery.siteId(1L).securityLevel(3);

		infoQuery.clean();

		logger.info("infoQuery 调用clean方法后 :{}",infoQuery.singleResult());
	}

	@Test
	public void queryByCode(){
		PageableResult<List<Info>> infos = infoService.createInfoQuery()
				.siteCode("newstrength")
				.channelCode("bank")
				.viewType(ApiParams.getVueType())
				.includeContent(false)
				.listPageByCode(1,15);

		logger.info(TranUtils.toJson(infos.getData()));
	}
}





