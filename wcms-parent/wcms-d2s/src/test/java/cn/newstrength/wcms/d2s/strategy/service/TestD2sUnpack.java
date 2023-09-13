package cn.newstrength.wcms.d2s.strategy.service;

import cn.newstrength.nsms.Application;
import cn.newstrength.wcms.d2s.dto.ParentD2sEvent;
import cn.newstrength.wcms.d2s.dto.channel.ChannelAddD2SEvent;
import cn.newstrength.wcms.d2s.dto.channel.ChannelCopyD2SEvent;
import cn.newstrength.wcms.d2s.message.D2sMessage;
import cn.newstrength.wcms.d2s.strategy.service.impl.ChannelAddUnpack;
import cn.newstrength.wcms.d2s.strategy.service.impl.ChannelCopyUnpack;
import cn.newstrength.wtdf.plugin.result.TranResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Auther: zrf
 * @Date: 2020/12/29 21:11
 * @Description: 测试拆包策略
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestD2sUnpack {
    @Autowired
    ChannelAddUnpack channelAddUnpack;
    @Autowired
    ChannelCopyUnpack channelCopyUnpack;

    /**
     * 测试添加栏目拆包策略
     */
    @Test
    public void testChannelAddUnpack() {
        ParentD2sEvent channelAddD2sEvent = new ChannelAddD2SEvent()
                .setSiteId(1)
                .setChannelId(1)
                .getParentTransForm()
                .setLoginId("admin").build();
        TranResult<List<D2sMessage>> tranResult = channelAddUnpack.unpack(channelAddD2sEvent);
    }

    @Test
    public void testChannelCopyUnpack() {
        ParentD2sEvent channelCopyD2sEvent = new ChannelCopyD2SEvent()
                .setSourceChannelId(new Long[]{0L, 1l, 2l})
                .setTargetSiteId(3)
                .setTargetChannelId(4)
                .getParentTransForm()
                .setLoginId("admin").build();
        TranResult<List<D2sMessage>> unpack = channelCopyUnpack.unpack(channelCopyD2sEvent);
    }
}