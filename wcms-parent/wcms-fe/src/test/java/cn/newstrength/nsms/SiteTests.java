package cn.newstrength.nsms;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import cn.newstrength.wcms.site.api.dto.Site;
import cn.newstrength.wcms.site.api.service.SiteService;
import cn.newstrength.wtdf.plugin.util.TranUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SiteTests {
	@Autowired
	private SiteService siteService;

	@Test
	public void config() {
		List<Site> sites = siteService.createSiteQuery().id(1L).list();
		System.out.println(TranUtils.toJson(sites));
		Assert.assertTrue(sites.size()>0);
	}
}
