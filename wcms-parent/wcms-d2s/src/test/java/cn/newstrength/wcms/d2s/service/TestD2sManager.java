package cn.newstrength.wcms.d2s.service;

import cn.newstrength.nsms.Application;
import cn.newstrength.wcms.d2s.dto.ParentD2sEvent;
import cn.newstrength.wcms.d2s.dto.channel.ChannelAddD2SEvent;
import cn.newstrength.wcms.d2s.message.D2sMessage;
import cn.newstrength.wcms.d2s.strategy.manager.UnpackStrategContext;
import cn.newstrength.wtdf.plugin.result.TranResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

/**
 * @Auther: zrf
 * @Date: 2020/12/22 11:31
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestD2sManager {
    @Autowired
    private  UnpackStrategContext unpackStrategContext;

    @Test
    public void testD2sUnpack(){
        String  task_uuid = UUID.randomUUID().toString();
        ParentD2sEvent channelAddD2sEvent  = new ChannelAddD2SEvent()
                .setSiteId(1)
                .setChannelId(1)
                .getParentTransForm()
                .setLoginId("admin")
                .setTaskUuid(task_uuid).build();
        // 此次任务调度的唯一标识
        TranResult<List<D2sMessage>> unpack = unpackStrategContext.unpack(channelAddD2sEvent);
    }
}
