package cn.newstrength.nsms;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import cn.newstrength.wcms.site.api.dto.Site;
import cn.newstrength.wcms.site.api.service.SiteService;
import cn.newstrength.wtdf.plugin.util.TranUtils;
import io.micrometer.core.instrument.util.IOUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SiteTests {
	@Autowired
	private SiteService siteService;

	@Test
	public void config() {
		
		//List<Site> sites = siteService.createSiteQuery().id(1L).list();
		//System.out.println(TranUtils.toJson(sites));
		//Assert.assertTrue(sites.size()>0);
	}
	
	public static void main(String[] args) {
		ClassPathResource resource = new ClassPathResource("template/site.ftl");
		try(InputStream inputStream = resource.getInputStream();){
			String script = IOUtils.toString(inputStream,StandardCharsets.UTF_8);
			System.out.println(script);
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}




