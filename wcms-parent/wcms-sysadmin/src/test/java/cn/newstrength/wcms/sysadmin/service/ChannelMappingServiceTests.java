/*
 * Copyright (c) 2020, NewStrength. All rights reserved.
 */

package cn.newstrength.wcms.sysadmin.service;

import cn.newstrength.nsms.Application;
import cn.newstrength.wcms.core.constant.InfoMappingStatus;
import cn.newstrength.wcms.core.constant.InfoMappingType;
import cn.newstrength.wcms.sysadmin.channelmapping.bo.ChannelMappingBO;
import cn.newstrength.wtdf.plugin.result.TranResult;
import cn.newstrength.wtdf.plugin.util.TranUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 站点栏目映射单元测试类
 * 
 * @author Xd
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ChannelMappingServiceTests {
	private static final Logger logger = LoggerFactory.getLogger(ChannelMappingServiceTests.class);

    @Autowired
    private ChannelMappingService channelMappingService;
    
    @Test
    public void queryAllByBizKey(){
    	TranResult<List<ChannelMappingBO>> tranResult = channelMappingService.queryChannelMappingByInfoId(42l);
    	logger.info("接口服务响应信息：{}",tranResult.toString());
    	List<ChannelMappingBO> list = tranResult.getData();
    	logger.info("{}",TranUtils.toJson(list));
    	//删除映射
    	logger.info("删除映射关系 {}",channelMappingService.deleteChannelMapping(118l));
    	//更新映射关系
    	logger.info("更新栏目映射日志状态 {}",channelMappingService.updateChannelMappingLogStatus(117l,InfoMappingStatus.YES));
    	//写入栏目映射关系
    	//logger.info("更新栏目映射日志状态 {}",channelMappingService.writeChannelMappingLog(117l,InfoMappingType.COPY,43l));
    	
    	
    }

}












