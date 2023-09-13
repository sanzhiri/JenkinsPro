package cn.newstrength.wcms.d2s.service;

import cn.newstrength.nsms.Application;
import cn.newstrength.wcms.d2s.bo.FileLogBo;
import cn.newstrength.wcms.d2s.model.FileLog;
import cn.newstrength.wcms.d2s.service.impl.FileLogServiceImpl;
import cn.newstrength.wtdf.plugin.param.TranParams;
import cn.newstrength.wtdf.plugin.result.TranResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: zrf
 * @Date: 2021/1/20 11:07
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestFileLogService {
    private static final Logger logger = LoggerFactory.getLogger(TestFileLogService.class);
    @Autowired
    public FileLogServiceImpl fileLogService;

    @Test
    public void testCreateBatch(){
        long start = System.currentTimeMillis();
        List<FileLogBo> fileLogBoList = new ArrayList<>();
        for(int i = 0 ; i<1 ;i++){
            FileLogBo fileLogBo = new FileLogBo();
            fileLogBo.setEventId(1);
            fileLogBo.setPublishType("site_index");
            fileLogBo.setFileName("index.shtml");
            fileLogBo.setFilePath("/newstrength/index");
            fileLogBo.setStatus("1");
            fileLogBo.setSiteId(100L);
            fileLogBoList.add(fileLogBo);
        }

        for(int j = 0 ; j < 20 ;j++){
            FileLogBo fileLogBo = new FileLogBo();
            fileLogBo.setEventId(1);
            fileLogBo.setPublishType("channel_list");
            fileLogBo.setFileName("list_"+j+1+".shtml");
            fileLogBo.setFilePath("/newstrength/news/");
            fileLogBo.setStatus("1");
            fileLogBo.setSiteId(100L);
            fileLogBo.setChannelId(200L);
            fileLogBo.setCurrentPage(1);
            fileLogBoList.add(fileLogBo);
        }
        for(int j = 0 ; j< 3;j++){
            FileLogBo fileLogBo = new FileLogBo();
            fileLogBo.setEventId(1);
            fileLogBo.setPublishType("channel_index");
            fileLogBo.setFileName("channelIndex_"+j+1+".shtml");
            fileLogBo.setFilePath("/newstrength/news/"+300+j);
            fileLogBo.setStatus("1");
            fileLogBo.setSiteId(100L);
            fileLogBo.setChannelId(200L);
            fileLogBo.setInfoId(300L+j);
            fileLogBoList.add(fileLogBo);
        }

        for(int j = 0 ; j< 3;j++){
            FileLogBo fileLogBo = new FileLogBo();
            fileLogBo.setEventId(1);
            fileLogBo.setPublishType("site_other");
            fileLogBo.setFileName(j+100+".shtml");
            fileLogBo.setFilePath("/newstrength/news/"+300+j);
            fileLogBo.setStatus("1");
            fileLogBo.setSiteId(100L);
            fileLogBo.setChannelId(200L);
            fileLogBo.setInfoId(300L+j);
            fileLogBo.setTemplateId(200+j);
            fileLogBoList.add(fileLogBo);
        }
        for(int j = 0 ; j< 3;j++){
            FileLogBo fileLogBo = new FileLogBo();
            fileLogBo.setEventId(j+1);
            fileLogBo.setPublishType("info_detail");
            fileLogBo.setFileName("other_"+j+100+".shtml");
            fileLogBo.setFilePath("/newstrength/news/"+300+j);
            fileLogBo.setStatus("1");
            fileLogBo.setSiteId(100L);
            fileLogBo.setChannelId(200L);
            fileLogBo.setInfoId(300L+j);
            fileLogBoList.add(fileLogBo);
        }

        TranParams<List<FileLogBo> > params = new TranParams<>(fileLogBoList);
        TranResult<Boolean> tranResult = fileLogService.createBatch(params);
        long end = System.currentTimeMillis();
        logger.info("批量插入:{};耗时:{}ms",tranResult.getData(),(end-start));
    }
    @Test
    public void testUpdate(){
        FileLog fileLog = new FileLog();
        fileLog.setStatus("0");
        fileLog.setId(60046L);
        TranParams<FileLog> fileLogTranParams = new TranParams<>(fileLog);
        TranResult<Boolean> update = fileLogService.update(fileLogTranParams);
        logger.info("更新:{}",update.getData());
    }
    @Test
    public void testGetByIdFileLog(){
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",60046);
        TranParams<Map<String,Object>> fileLogTranParams = new TranParams<>(paramsMap);
        TranResult<FileLogBo> tranResult = fileLogService.getFileLogById(fileLogTranParams);
        logger.info("获取文件记录对象:{}",tranResult.getData().toString());
    }
}
