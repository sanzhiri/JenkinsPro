/*
 * Copyright (c) 2020, NewStrength. All rights reserved.
 */

package cn.newstrength.wcms.sysadmin.service;

import cn.newstrength.nsms.Application;
import cn.newstrength.wcms.core.constant.*;
import cn.newstrength.wcms.sysadmin.flow.service.FlowRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 类描述
 * <p>详细描述</p>
 *
 * @author kyrie 2021/3/7 8:36 下午
 * @since jdk1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class FlowRecordServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(FlowRecordServiceTest.class);

    @Autowired
    private FlowRecordService flowRecordService;

    @Test
    public void addTest(){
        Long bizKey = 2L;

        int result = flowRecordService.addFlowRecord(bizKey,BizType.INFO,"root",FlowStatus.PROCESS, Action.DELETE);
        logger.info("测试插入流程记录结果：{}",result);
    }

    @Test
    public void updateTest(){
        Long bizKey = 1L;

        int result = flowRecordService.updateFlowRecord(bizKey,BizType.INFO, FlowStatus.FINISHED,
                FlowElementType.END.getValue(), FlowApprovalResult.DISAGREE.getValue(),"SB hehe","JAn",Action.DELETE);
        logger.info("测试修改流程记录结果：{}",result);
    }

    @Test
    public void deleteTest(){
        Long bizKey = 1L;
        int result = flowRecordService.deleteFlowRecordByBiz(bizKey,BizType.INFO);
        logger.info("测试删除流程记录：{}",result);
    }
}
