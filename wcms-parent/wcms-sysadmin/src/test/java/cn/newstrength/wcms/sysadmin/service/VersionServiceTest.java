/*
 * Copyright (c) 2020, NewStrength. All rights reserved.
 */

package cn.newstrength.wcms.sysadmin.service;

import cn.newstrength.nsms.Application;
import cn.newstrength.wcms.core.constant.BizType;
import cn.newstrength.wcms.sysadmin.info.bo.InfoBO;
import cn.newstrength.wcms.sysadmin.util.JSONUtils;
import cn.newstrength.wcms.sysadmin.version.bo.VersionBO;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 类描述
 * <p>详细描述</p>
 *
 * @author kyrie 2020/11/1 9:50 下午
 * @since jdk1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class VersionServiceTest {

    @Autowired
    private VersionService versionService;

    @Test
    public void createTest(){
        VersionBO versionBO = new VersionBO();
        versionBO.setVer(2);
        versionBO.setContent("12312312");

        System.out.println("成功插入历史版本："+versionService.create(versionBO, BizType.INFO, 3L, "rroot"));
    }

    @Test
    public void queryTemp(){
        Long bizKey = 13L;

        String content = versionService.queryTemp(bizKey,BizType.INFO_TEMP);
        Assert.assertNotNull(content);
        InfoBO infoBO = JSONUtils.jsonToBeanByTypeReference(content, new TypeReference<InfoBO>() {});
        System.out.println(content);
        System.out.println(infoBO.toString());
    }
}
