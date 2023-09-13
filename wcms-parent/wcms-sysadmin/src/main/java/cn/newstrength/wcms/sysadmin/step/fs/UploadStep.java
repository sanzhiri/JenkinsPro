//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.newstrength.wcms.sysadmin.step.fs;

import cn.newstrength.wcms.sysadmin.service.FileSystemService;
import cn.newstrength.wtdf.plugin.obj.JWTSubject;
import cn.newstrength.wtdf.plugin.result.TranResult;
import cn.newstrength.wtdf.web.exception.PAIException;
import cn.newstrength.wtdf.web.mybatis.service.impl.MapperServiceImpl;
import cn.newstrength.wtdf.web.processor.Operation;
import cn.newstrength.wtdf.web.processor.OperationStep;
import cn.newstrength.wtdf.web.util.StepUtils;
import java.util.Map;
import org.apache.commons.collections.MapUtils;

public class UploadStep implements OperationStep {
    public UploadStep() {
    }

    public int excute(Operation oper) throws PAIException {
        String extensions = oper.getContext().getElementValue("extensions");
        MapperServiceImpl service = oper.getBean("mapperService", MapperServiceImpl.class);
        FileSystemService fileSystemService = (FileSystemService)oper.getBean("fileSystemService", FileSystemService.class);
        Map<String, Object> input = StepUtils.getInputValue(oper);
        String path = MapUtils.getString(input, "path");
        String sign = MapUtils.getString(input, "sign");
        Long siteId = MapUtils.getLong(input, "site_id", 0L);
        JWTSubject jwt = (JWTSubject)input.get("_user");
        TranResult<Boolean> tranResult = fileSystemService.upload(jwt, oper.getRequest(), extensions.split(","), siteId, path,sign,service);
        oper.setTranResult(tranResult);
        return 0;
    }

    public int init(Map<String, String> param) {
        return 0;
    }
}
