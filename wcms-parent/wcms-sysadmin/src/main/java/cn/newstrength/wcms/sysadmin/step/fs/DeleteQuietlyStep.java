package cn.newstrength.wcms.sysadmin.step.fs;

import java.util.Map;
import org.apache.commons.collections.MapUtils;
import cn.newstrength.wcms.sysadmin.service.FileSystemService;
import cn.newstrength.wtdf.plugin.obj.JWTSubject;
import cn.newstrength.wtdf.plugin.result.TranResult;
import cn.newstrength.wtdf.web.exception.PAIException;
import cn.newstrength.wtdf.web.processor.Operation;
import cn.newstrength.wtdf.web.processor.OperationStep;
import cn.newstrength.wtdf.web.util.StepUtils;

public class DeleteQuietlyStep implements OperationStep {

	@Override
	public int excute(Operation oper) throws PAIException {
		FileSystemService fileSystemService = oper.getBean("fileSystemService", FileSystemService.class);
		Map<String, Object> input = StepUtils.getInputValue(oper);
		String id = MapUtils.getString(input, "id");
		Long siteId = MapUtils.getLong(input, "site_id",0l);
		JWTSubject jwt = (JWTSubject) input.get("_user");
		TranResult<Boolean> tranResult = fileSystemService.deleteQuietly(jwt, siteId,id);
		oper.setTranResult(tranResult);
		return 0;
	}

	@Override
	public int init(Map<String, String> param) {
		return 0;
	}
}
