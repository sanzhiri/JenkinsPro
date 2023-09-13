package cn.newstrength.logger.wcms.audit.org;

import cn.newstrength.wtdf.plugin.param.TranParams;
import cn.newstrength.wtdf.plugin.result.TranResult;
import cn.newstrength.wtdf.web.databus.DataContext;
import cn.newstrength.wtdf.web.exception.PAIException;
import cn.newstrength.wtdf.web.mybatis.service.impl.MapperServiceImpl;
import cn.newstrength.wtdf.web.processor.Operation;
import cn.newstrength.wtdf.web.processor.OperationStep;

import java.util.List;
import java.util.Map;

public class OrgStep implements OperationStep {

    @Override
    public int excute(Operation oper) throws PAIException {
        DataContext context = oper.getContext();
        String resource = context.getElementValue("resource");
        TranParams params = new TranParams();
        params.setResource(resource);
        MapperServiceImpl service = oper.getBean("mapperService", MapperServiceImpl.class);
        TranResult<List> tranResult = service.selectList(params);
        List data = tranResult.getData();
        oper.setTranStepSuccResult(data);
        return 0;
    }

    @Override
    public int init(Map<String, String> param) {
        return 0;
    }
}
