package cn.newstrength.nsms.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.output.StringBuilderWriter;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerHelper {
	
	private FreemarkerHelper() {
		
	}
	
	public static <T> String getTemplate(Configuration configuration, String scripts, T args) {
		StringTemplateLoader stl = new StringTemplateLoader();
		stl.putTemplate("name", scripts);
		configuration.setTemplateLoader(stl);
		String html = "";
		try (Writer writer = new StringBuilderWriter();) {
			Template template = configuration.getTemplate("name", StandardCharsets.UTF_8.name());
			template.process(args, writer);
			html = writer.toString();
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
		return html;
	}

}
