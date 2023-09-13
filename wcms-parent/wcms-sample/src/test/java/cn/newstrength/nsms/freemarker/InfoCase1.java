package cn.newstrength.nsms.freemarker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import cn.newstrength.wcms.d2s.api.dto.TemplateDefinition;
import cn.newstrength.wcms.d2s.api.service.D2sService;
import cn.newstrength.wtdf.plugin.util.TranUtils;
import freemarker.template.Configuration;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InfoCase1 {
	private static final Logger logger = LoggerFactory.getLogger(InfoCase1.class);
	@Autowired
	private D2sService d2sService;
	@Autowired
	private Configuration configuration;

	@Test
	public void writer() {
		
		TemplateDefinition templateDefinition = d2sService.createD2sQuery().infoId(9l).infoTemplateDefinition();
		logger.info("解析后：{}", TranUtils.toJson(templateDefinition));
		
//		Template template = templateService.createTemplateQuery().id(6l).singleResult();
//		String scripts = template.getScripts();
//		logger.info("模板代码：{}",scripts);
//		logger.info("-------------------------------------------");
//		//获取模板内容
//		TemplateDataModel templateDataModel = new TemplateDataModel();
//		templateDataModel.setInfoId(10l);
//		TranResult<String> html = TemplateUtils.getTemplate(configuration, scripts, templateDataModel.getDataModel());
//		logger.info("解析后：{}", html.getData());
	}
}




















