package cn.newstrength.nsms.api;

import cn.newstrength.wcms.d2s.api.helper.D2sHelper;
import cn.newstrength.wcms.info.api.dto.Info;
import cn.newstrength.wcms.info.api.query.InfoQuery;
import cn.newstrength.wcms.info.api.service.InfoService;
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
public class D2sTests {
	private final static Logger logger = LoggerFactory.getLogger(D2sTests.class);
	
	@Test
	public void getInfoStaticPath() {
		String infoStaticPath = D2sHelper.getInfoStaticPath(289l, 291l, 55l, "2021-01-28 16:13:38");
		logger.info("信息 : {}",infoStaticPath);
	}
}





