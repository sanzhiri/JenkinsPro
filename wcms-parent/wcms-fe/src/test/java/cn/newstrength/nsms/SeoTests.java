package cn.newstrength.nsms;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import cn.newstrength.wcms.seo.api.dto.Seo;
import cn.newstrength.wcms.seo.api.service.SeoService;
import cn.newstrength.wtdf.plugin.util.TranUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeoTests {
	@Autowired
	private SeoService seoService;

	@Test
	public void testBySite() {
		Seo seo = seoService.createSeoQuery().siteId(1L).singleResult();
		System.out.println(TranUtils.toJson(seo));
		Assert.assertTrue(seo!=null);
	}
}






