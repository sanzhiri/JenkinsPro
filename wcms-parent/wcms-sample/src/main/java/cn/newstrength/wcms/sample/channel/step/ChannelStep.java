package cn.newstrength.wcms.sample.channel.step;

import java.util.List;
import java.util.Map;

import cn.newstrength.wcms.channel.api.dto.Channel;
import cn.newstrength.wcms.channel.api.service.ChannelService;
import cn.newstrength.wcms.channel.api.service.impl.ChannelServiceImpl;
import cn.newstrength.wtdf.web.exception.PAIException;
import cn.newstrength.wtdf.web.processor.Operation;
import cn.newstrength.wtdf.web.processor.OperationStep;

public class ChannelStep implements OperationStep {

	@Override
	public int excute(Operation oper) throws PAIException {
		ChannelService channelService = oper.getBean("channelService", ChannelServiceImpl.class);
		List<Channel> channel = channelService.createChannelQuery().siteId(198l).formatToTree(true).list();
		oper.setTranStepSuccResult(channel);
		return 0;
	}

	@Override
	public int init(Map<String, String> param) {
		return 0;
	}

}
