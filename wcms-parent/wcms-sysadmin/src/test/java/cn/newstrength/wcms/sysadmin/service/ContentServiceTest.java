/*
 * Copyright (c) 2020, NewStrength. All rights reserved.
 */

package cn.newstrength.wcms.sysadmin.service;

import cn.newstrength.nsms.Application;
import cn.newstrength.wcms.core.constant.BizType;
import cn.newstrength.wcms.core.constant.FileType;
import cn.newstrength.wcms.sysadmin.content.model.Content;
import cn.newstrength.wtdf.plugin.result.TranResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述
 * <p>详细描述</p>
 *
 * @author kyrie 2020/10/28 10:15 上午
 * @since jdk1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ContentServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(ContentServiceTest.class);
    @Autowired
    private BizContentService bizContentService;


    @Test
    public void queryAllByBizKey(){
        System.out.println(bizContentService.queryAllByBizKey(BizType.INFO, 7L).size());
    }

    @Test
    public void delete(){
        TranResult<Boolean> result = bizContentService.delete(BizType.INFO,2L, FileType.CONTENT);
        Assert.assertNotNull(result.getData());
        System.out.println("删除结果为："+result.getData());
    }

    @Test
    public void deleteByType(){
        TranResult<Boolean> result = bizContentService.deleteByBizKey(BizType.INFO,2L);
        Assert.assertNotNull(result.getData());
        System.out.println("删除结果为："+result.getData());
    }

    @Test
    public void updateFileSortNum(){
        int result = bizContentService.updateFileSortNumById(BizType.INFO,2L,FileType.CONTENT,1L,3,"wangrui");
        Assert.assertTrue(result > 0);
        System.out.println("更新文件排序成功");
    }
}
