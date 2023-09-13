/*
 * Copyright (c) 2020, NewStrength. All rights reserved.
 */

package cn.newstrength.wcms.sysadmin.service;

import cn.newstrength.nsms.Application;
import cn.newstrength.wcms.core.constant.SourceType;
import cn.newstrength.wcms.sysadmin.service.SourceCleanService;
import cn.newstrength.wcms.sysadmin.sourceclean.model.SourceClean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述
 * <p>详细描述</p>
 *
 * @author kyrie 2020/10/28 9:30 上午
 * @since jdk1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SourceCleanServiceTest {

    @Autowired
    private SourceCleanService sourceCleanService;

    @Test
    public void testBatchCreate(){
        List<SourceClean> sources = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SourceClean source = new SourceClean();
            source.setFilePath("/2020/10/123.txt");
            source.setCreator("root");
            source.setSourceType(SourceType.CONTENT.getValue());
            source.setSiteId(1L);
            source.setOriginalContent("{\"key\":\"我是key\"}");

            sources.add(source);
        }

        System.out.println(sourceCleanService.batchAddCleanSource(sources));
    }
}
