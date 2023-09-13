package cn.newstrength.wcms.d2s.service;

import cn.newstrength.nsms.Application;
import cn.newstrength.wcms.d2s.dto.info.ChannelInfoValidateTimeD2sEvent;
import cn.newstrength.wcms.d2s.queue.D2SEventQueue;
import cn.newstrength.wtdf.plugin.util.TranUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: zrf
 * @Date: 2020/12/25 14:52
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestParentD2SEventService {
    public static final Logger logger = LoggerFactory.getLogger(TestParentD2SEventService.class);

    @Autowired
    private D2SEventQueue d2SEventQueue;
    @Test
    public void testD2sEventPublish() throws InterruptedException {

        Map<String, List<Integer>> channelInfoMap= new HashMap<>();
        List<Integer> infoList = new ArrayList<>();
        infoList.add(2);
        infoList.add(3);

        List<Integer> infoList1 = new ArrayList<>();
        infoList1.add(5);
        infoList1.add(6);
        // key:为栏目ID   value:栏目下信息ID集合
        channelInfoMap.put("1",infoList);
        channelInfoMap.put("4",infoList1);

        String channelInfoMapping = TranUtils.toJson(channelInfoMap);
        logger.info(channelInfoMapping);
        Map<String, List<Long>> listMap = TranUtils.jsonToBean(channelInfoMapping);
        logger.info(listMap.toString());
        ChannelInfoValidateTimeD2sEvent channelInfoValidateTimeD2sEvent =  new ChannelInfoValidateTimeD2sEvent()
                .setSiteId(1)
                .setChannelInfoMap(channelInfoMap)
                .getParentTransForm()
                .setLoginId("admin").build();
        d2SEventQueue.produce(channelInfoValidateTimeD2sEvent);
        logger.info("事件消息入队列成功！");
    }
}
