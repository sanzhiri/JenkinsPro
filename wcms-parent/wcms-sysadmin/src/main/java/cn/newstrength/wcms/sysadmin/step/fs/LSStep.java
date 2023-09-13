//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.newstrength.wcms.sysadmin.step.fs;

import cn.newstrength.wcms.sysadmin.service.FileSystemService;
import cn.newstrength.wtdf.plugin.obj.JWTSubject;
import cn.newstrength.wtdf.web.exception.PAIException;
import cn.newstrength.wtdf.web.processor.Operation;
import cn.newstrength.wtdf.web.processor.OperationStep;
import cn.newstrength.wtdf.web.util.StepUtils;
import java.util.Map;
import org.apache.commons.collections.MapUtils;

public class LSStep implements OperationStep {
    public LSStep() {
    }

    public int excute(Operation oper) throws PAIException {
        FileSystemService fileSystemService = (FileSystemService)oper.getBean("fileSystemService", FileSystemService.class);
        Map<String, Object> input = StepUtils.getInputValue(oper);
        String path = MapUtils.getString(input, "path", "/");
        boolean hidden = MapUtils.getBooleanValue(input, "hidden");
        Long siteId = MapUtils.getLong(input, "site_id", 0L);
        JWTSubject jwt = (JWTSubject)input.get("_user");
        int currentPage = MapUtils.getIntValue(input, "currentPage", 1);
        int pageSize = MapUtils.getIntValue(input, "pageSize", 10);
        oper.setTranResult(fileSystemService.ls(jwt, siteId, path, hidden,currentPage,pageSize));
        return 0;
    }

    public int init(Map<String, String> param) {
        return 0;
    }
}
