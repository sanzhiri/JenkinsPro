package cn.newstrength.nsms.freemarker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import cn.newstrength.wcms.template.api.service.TemplateService;
import cn.newstrength.wtdf.plugin.result.TranResult;
import cn.newstrength.wtdf.plugin.util.TranUtils;
import freemarker.template.Configuration;

@RunWith(SpringRunner.class)
@SpringBootTest
public class D2sCase01 {
	private static final Logger logger = LoggerFactory.getLogger(D2sCase01.class);
//	@Autowired
//	private D2sExecute d2sExecute;
//	
//	//@Test
//	public void siteIndexHtml() {
//		TranResult<Html> tranResult = d2sExecute.getSiteIndexHtml(20);
//		logger.info(TranUtils.toJson(tranResult));
//	}
//	
//	//@Test
//	public void channelIndexHtml() {
//		TranResult<Html> tranResult = d2sExecute.getChannelIndexHtml(1,20);
//		//测试
//		logger.info(TranUtils.toJson(tranResult));
//	}	
//	@Test
//	public void infoTemplate() {
//		TranResult<Html> tranResult = d2sExecute.getInfoHtml(11,120,9);
//		logger.info(TranUtils.toJson(tranResult));
//	}
//	
	public void listTemplate() {
//		TranResult<Html> tranResult = d2sExecute.getChannelListHtml(1, 2,1,100);
//		
//		d2sExecute.getSiteIndexHtml(20);
//		d2sExecute.getChannelListHtml(1, 2,1,100);
//		d2sExecute.getInfoHtml(11,120,9);
	}
	
	
	//
	
	
	
//
//	@Test
//	public void siteOtherTemplateDefinition() {
//		TranResult<List<String>> tranResult = d2sExecute.getSiteOtherHtml(1l);
//	}
//	
//	
//	public void writer() {
//		Template template = templateService.createTemplateQuery().id(4l).singleResult();
//		String scripts = template.getScripts();
//		logger.info("模板代码：{}",scripts);
//		logger.info("-------------------------------------------");
//		//获取模板内容
//		Map<String, String> data = Maps.newHashMap();
//		String html = FreemarkerHelper.getTemplate(configuration, scripts, data);
//		logger.info("解析后：{}", html);
//	}


}
