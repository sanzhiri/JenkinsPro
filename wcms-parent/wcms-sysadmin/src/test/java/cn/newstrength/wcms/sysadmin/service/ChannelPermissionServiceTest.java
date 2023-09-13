/*
 * Copyright (c) 2020, NewStrength. All rights reserved.
 */

package cn.newstrength.wcms.sysadmin.service;

import cn.newstrength.nsms.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 栏目权限单元测试类
 * <p>栏目权限单元测试类</p>
 *
 * @author kyrie 2020/11/25 11:05 上午
 * @since jdk1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ChannelPermissionServiceTest {

    @Autowired
    private ChannelPermissionService channelPermissionService;

    /**
     * 根据用户查询栏目列表
     */
    @Test
    public void getChannelIdsByUser(){
        Map<String,Object> param = new HashMap<>();
        param.put("id",1);
        param.put("type","user");
        List<Long> channelIds = channelPermissionService.queryChannelIdsAPI(param);
        Assert.assertNotNull(channelIds);
        System.out.println("绑定栏目数"+channelIds.size());
    }
}
