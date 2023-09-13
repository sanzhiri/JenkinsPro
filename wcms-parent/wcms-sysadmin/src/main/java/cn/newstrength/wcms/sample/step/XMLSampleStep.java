package cn.newstrength.wcms.sample.step;

import java.util.Map;
import cn.newstrength.wtdf.plugin.result.SuccessTranResult;
import cn.newstrength.wtdf.web.exception.PAIException;
import cn.newstrength.wtdf.web.processor.Operation;
import cn.newstrength.wtdf.web.processor.OperationStep;

public class XMLSampleStep implements OperationStep {

	@Override
	public int excute(Operation oper) throws PAIException {
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.append("<user>");
		xml.append("<name>浪聪</name>");
		xml.append("</user>");
		oper.setTranResult(new SuccessTranResult<>(xml));
		return 0;
	}

	@Override
	public int init(Map<String, String> param) {
		return 0;
	}

}
