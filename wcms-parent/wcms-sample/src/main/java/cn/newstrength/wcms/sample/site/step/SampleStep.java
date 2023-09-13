package cn.newstrength.wcms.sample.site.step;

import java.util.Map;

import cn.newstrength.wcms.sample.site.obj.SampleObj;
import cn.newstrength.wtdf.plugin.service.RedisService;
import cn.newstrength.wtdf.web.exception.PAIException;
import cn.newstrength.wtdf.web.processor.Operation;
import cn.newstrength.wtdf.web.processor.OperationStep;

public class SampleStep implements OperationStep {

	@Override
	public int excute(Operation oper) throws PAIException {
		RedisService redisService = oper.getBean("redisService", RedisService.class);
		SampleObj sampleObj = new SampleObj(1L,"Koma");
		redisService.put("sample", sampleObj);
		//获取数据
		SampleObj obj = redisService.getByKey("sample");
		
		oper.setTranStepSuccResult("OK");
		return 0;
	}

	@Override
	public int init(Map<String, String> param) {
		return 0;
	}

}
