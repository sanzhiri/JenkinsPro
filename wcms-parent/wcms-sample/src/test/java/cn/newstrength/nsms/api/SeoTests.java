/*
 * Copyright (c) 2020, NewStrength. All rights reserved.
 */

package cn.newstrength.nsms.api;

import cn.newstrength.wcms.seo.api.dto.Seo;
import cn.newstrength.wcms.seo.api.service.SeoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeoTests {
    private final static Logger logger = LoggerFactory.getLogger(SeoTests.class);

    @Autowired
    private SeoService seoService;

    @Test
    public void singleResult(){
        Long infoId = 11L;
        Seo seo = seoService.createSeoQuery().infoId(infoId).singleResult();
        logger.info("信息：{} 的seo信息为 {}",infoId,seo.toString());
    }
}
