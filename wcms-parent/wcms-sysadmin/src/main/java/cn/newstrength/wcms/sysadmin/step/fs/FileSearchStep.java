package cn.newstrength.wcms.sysadmin.step.fs;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import cn.newstrength.wcms.sysadmin.service.FileSearchService;
import cn.newstrength.wcms.sysadmin.service.FileSystemService;
import cn.newstrength.wtdf.plugin.obj.JWTSubject;
import cn.newstrength.wtdf.web.exception.PAIException;
import cn.newstrength.wtdf.web.processor.Operation;
import cn.newstrength.wtdf.web.processor.OperationStep;
import cn.newstrength.wtdf.web.util.StepUtils;
import java.util.Map;
import org.apache.commons.collections.MapUtils;

public class FileSearchStep implements OperationStep {
    public FileSearchStep() {
    }

    public int excute(Operation oper) throws PAIException {
        FileSearchService fileSearchService = (FileSearchService)oper.getBean("fileSearchService", FileSearchService.class);
        Map<String, Object> input = StepUtils.getInputValue(oper);
        String path = MapUtils.getString(input, "path", "/");
        /*
   <string name="pageSize" desc="每页条数" />
				<string name="currentPage" desc="当前页码" />
         */
        String creator = MapUtils.getString(input, "creator", null);
        String wjtype = MapUtils.getString(input, "wjtype", null);
        String searchpath = MapUtils.getString(input, "searchpath", "/");
        boolean hidden = MapUtils.getBooleanValue(input, "hidden");
        Long siteId = MapUtils.getLong(input, "site_id", 0L);
        int pageSize =  MapUtils.getIntValue(input, "pageSize", 10);
        Long currentPage =  MapUtils.getLong(input, "currentPage", 1L);
        JWTSubject jwt = (JWTSubject)input.get("_user");
        oper.setTranResult(fileSearchService.ls(jwt, siteId, path, hidden,creator,wjtype,searchpath,pageSize,currentPage));
        return 0;
    }

    public int init(Map<String, String> param) {
        return 0;
    }
}

