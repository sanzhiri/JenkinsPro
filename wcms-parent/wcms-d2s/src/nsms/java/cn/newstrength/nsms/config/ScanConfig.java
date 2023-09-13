package cn.newstrength.nsms.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: zrf
 * @Date: 2020/12/10 11:36
 * @Description:
 */
@Configuration
@ComponentScan({
        "cn.newstrength.wcms.d2s",
        "cn.newstrength.wcms.job",
        "cn.newstrength.wcms.tablib",
        "cn.newstrength.wcms.site",
        "cn.newstrength.wcms.template",
        "cn.newstrength.wcms.channel",
        "cn.newstrength.wcms.info"
})
public class ScanConfig {

}