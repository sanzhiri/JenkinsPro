package cn.newstrength.wcms.d2s.service;

import cn.newstrength.nsms.Application;
import cn.newstrength.wcms.core.constant.D2SEventType;
import cn.newstrength.wcms.d2s.bo.EventLogBo;
import cn.newstrength.wcms.d2s.service.impl.EventLogServiceImpl;
import cn.newstrength.wtdf.plugin.param.TranParams;
import cn.newstrength.wtdf.plugin.result.TranResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Auther: zrf
 * @Date: 2021/1/20 10:43
 * @Description: 测试动转静事件服务
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestEventLogService {

    private static final Logger logger = LoggerFactory.getLogger(TestEventLogService.class);

    @Autowired
    public EventLogServiceImpl eventLogService;
    @Test
    public void testCreate(){
        EventLogBo eventLogBo = new EventLogBo();
        eventLogBo.setCreator("admin");
        eventLogBo.setTaskType("2");
        eventLogBo.setEventStatus("2");
        eventLogBo.setSiteId(100);
        eventLogBo.setTaskUuid(UUID.randomUUID().toString().replaceAll("-",""));
        TranParams<EventLogBo> params = new TranParams<>(eventLogBo);
        TranResult<Long> integerTranResult = eventLogService.create(params);
        logger.info("插入信息ID:"+integerTranResult.getData());
    }
    @Test
    public void testUpdateRuning(){
        Map<String,Object> params = new HashMap<>();
        params.put("publishAction", D2SEventType.CHANNEL_ADD.getDesc());
        params.put("total",1000L);
        params.put("eventStatus","2");
        params.put("id",5);
        params.put("taskUuid","41da43a448784b0694693df1371fbe9b");
        TranResult<Boolean> updateResult = eventLogService.update(params);
        logger.info("更新信息:{}",updateResult.getData());
    }
    @Test
    public void testUpdateFinish(){
        Map<String,Object> params = new HashMap<>();
        params.put("id",5);
        params.put("taskUuid","41da43a448784b0694693df1371fbe9b");
        params.put("successNum",20000L);
        params.put("totalTime",2000L);
        params.put("finishTime",System.currentTimeMillis());
        params.put("eventStatus","3");
        TranResult<Boolean> updateResult = eventLogService.update(params);
        logger.info("更新信息:{}",updateResult.getData());
    }
    @Test
    public void testDelete(){
        Map<String,Object> parasMap = new HashMap<>();
        parasMap.put("id",3);
        parasMap.put("taskUuid","637371778c664e81a34af15cd7bd4827");
        TranParams<Map<String,Object>> params = new TranParams<>(parasMap);
        TranResult<Boolean> tranResult = eventLogService.delete(params);
        logger.info("删除信息:{}",tranResult.getData());
    }
    @Test
    public void testEventLogListPage(){
        Map<String,Object> parasMap = new HashMap<>();

    }
}
