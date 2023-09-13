/*
 * Copyright (c) 2020, NewStrength. All rights reserved.
 */

package cn.newstrength.wcms.sysadmin.service;

import cn.newstrength.nsms.Application;
import cn.newstrength.wcms.core.constant.FlowStatus;
import cn.newstrength.wcms.sysadmin.info.model.Info;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 类描述
 * <p>详细描述</p>
 *
 * @author kyrie 2020/10/30 4:48 下午
 * @since jdk1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class InfoServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(InfoServiceTest.class);

    @Autowired
    private InfoService infoService;

    @Test
    public void copyForChannel(){
        Long sourceChannel = 3L;
        Long targetChannel = 4L;
        Long targetSiteId = 1L;
        String loginId = "root";

        int result = infoService.copyInfoByChannelId(sourceChannel,targetChannel,targetSiteId,loginId);
        logger.info("拷贝成功{}条信息",result);
    }

    @Test
    public void updateFlowStatueTest(){
        infoService.updateFlowStatue(9L,"wangrui", FlowStatus.PROCESS,null);
    }
}
