//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.newstrength.logger.wcms.service.impl;

import cn.newstrength.logger.wcms.service.QueryService;
import cn.newstrength.wtdf.plugin.result.PageableResult;
import cn.newstrength.wtdf.plugin.result.SuccessTranResult;
import cn.newstrength.wtdf.plugin.result.TranResult;
import cn.newstrength.wtdf.plugin.util.DateUtils;
import com.github.pagehelper.PageRowBounds;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.apache.commons.collections.MapUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;

public class WCMSServiceImpl implements QueryService {
    private static final String DEFAULT_SERVICE_ID = "WCMS-SYSADMIN";
    @Autowired
    private DiscoveryClient discoveryClient;
    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    public WCMSServiceImpl() {
    }

    public TranResult<Map<String, Object>> query(Map<String, Object> args) {
        Map<String, Object> data = new HashMap();
        Map<String, Object> filter = new HashMap();
        List<String> services = new ArrayList();
        Iterator var5 = this.discoveryClient.getServices().iterator();

        while(var5.hasNext()) {
            String serviceId = (String)var5.next();
            services.add(serviceId.toUpperCase());
        }

        filter.put("services", services);
        List<Integer> err = new ArrayList();
        err.add(-1);
        err.add(-2);
        err.add(-3);
        err.add(-4);
        err.add(-5);
        err.add(-9);
        filter.put("errCode", err);
        List<String> tableDates = this.sqlSessionTemplate.selectList("cn.newstrength.logger.mapper.LoggerMapper.queryTableDate");
        filter.put("tableDate", tableDates);
        String errCode = MapUtils.getString(args, "errCode", "999999");
        String logType = MapUtils.getString(args, "logType", "01");
        if ("01".equals(logType)) {
            errCode = "0";
        }

        args.put("errCode", errCode);
        int currentPage = MapUtils.getIntValue(args, "currentPage", 1);
        int pageSize = MapUtils.getIntValue(args, "pageSize", 20);
        String serviceId = MapUtils.getString(args, "serviceId", "WCMS-SYSADMIN");
        //String tableDate = MapUtils.getString(args, "tableDate", DateUtils.format("yyyyMM"));
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 定义日期格式化器，格式化为"yyyyMM"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        // 将日期进行转化为指定格式
        String tableDate = currentDate.format(formatter);
        // 解析日期字符串
        YearMonth yearMonth = YearMonth.parse(tableDate, formatter);
        YearMonth Minus1 = yearMonth.minusMonths(1);//前一个月
        String MonthMinus1 = Minus1.format(formatter);
        YearMonth Minus2 = yearMonth.minusMonths(2);//前两个月
        String MonthMinus2 = Minus2.format(formatter);
        YearMonth Minus3 = yearMonth.minusMonths(3);
        String MonthMinus3 = Minus3.format(formatter);
        YearMonth Minus4 = yearMonth.minusMonths(4);
        String MonthMinus4 = Minus4.format(formatter);
        YearMonth Minus5 = yearMonth.minusMonths(5);
        String MonthMinus5 = Minus5.format(formatter);
        int offset = (currentPage - 1) * pageSize;
        PageRowBounds prb = new PageRowBounds(offset, pageSize);
        args.put("tableDate", tableDate);
        if (tableDates.contains(MonthMinus1)) {
            args.put("MonthMinus1", MonthMinus1);
        }else {
            args.put("MonthMinus1", tableDate);
        }
        if (tableDates.contains(MonthMinus2)) {
            args.put("MonthMinus2", MonthMinus2);
        }else {
            args.put("MonthMinus2", tableDate);
        }
        if (tableDates.contains(MonthMinus3)) {
            args.put("MonthMinus3",MonthMinus3);
        }else {
            args.put("MonthMinus3",tableDate);
        }
        if (tableDates.contains(MonthMinus4)){
            args.put("MonthMinus4",MonthMinus4);
        }else {
            args.put("MonthMinus4",tableDate);
        }
        if (tableDates.contains(MonthMinus5)){
            args.put("MonthMinus5",MonthMinus5);
        }else {
            args.put("MonthMinus5",tableDate);
        }
        System.out.println("tableDate=================>"+tableDate);
        System.out.println("MonthMinus1=================>"+MonthMinus1);
        System.out.println("MonthMinus5=================>"+MonthMinus5);
        args.put("serviceId", serviceId);
        List<Map<String, Object>> list = this.sqlSessionTemplate.selectList("cn.newstrength.logger.mapper.LoggerMapper.query", args, prb);
        int pageCount = (int)((prb.getTotal() - 1L) / (long)pageSize + 1L);
        PageableResult<List<Map<String, Object>>> pr = new PageableResult();
        pr.setCurrentPage(currentPage);
        pr.setPageSize(pageSize);
        pr.setRowCount(prb.getTotal());
        pr.setPageCount(pageCount);
        pr.setData(list);
        data.put("filter", filter);
        data.put("rows", pr);
        return new SuccessTranResult(data);
    }
}
