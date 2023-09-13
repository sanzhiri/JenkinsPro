package cn.newstrength.wcms.sample.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.common.collect.Maps;
import cn.newstrength.wcms.template.api.dto.Template;
import cn.newstrength.wcms.template.api.service.TemplateService;
import freemarker.template.Configuration;

@Controller
public class TaglibController {
	@Autowired
	private Configuration configuration;
	@Autowired
	private TemplateService templateService;
	
	@RequestMapping(value = "/freemaker/sample", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String gen(){
		Template template = templateService.createTemplateQuery().id(4l).singleResult();
		String scripts = template.getScripts();
		Map<String, String> data = Maps.newHashMap();
		data.put("name", "koma");
		String html = FreemarkerHelper.getTemplate(configuration, scripts, data);
		try {
			FileUtils.write(new File("/home/koma/aaa.shtml"), html);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "OK";
	}
}
