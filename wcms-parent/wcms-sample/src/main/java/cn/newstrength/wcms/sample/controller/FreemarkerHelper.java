package cn.newstrength.wcms.sample.controller;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.output.StringBuilderWriter;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class FreemarkerHelper {
	
	private FreemarkerHelper() {
		
	}
	
	public static <T> String getTemplate(Configuration configuration, String scripts, T t) {
		StringTemplateLoader stl = new StringTemplateLoader();
		stl.putTemplate("name", scripts);
		configuration.setTemplateLoader(stl);
		String html = "";
		try (Writer writer = new StringBuilderWriter();) {
			Template template = configuration.getTemplate("name", StandardCharsets.UTF_8.name());
			template.process(t, writer);
			html = writer.toString();
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
		return html;
	}

}
