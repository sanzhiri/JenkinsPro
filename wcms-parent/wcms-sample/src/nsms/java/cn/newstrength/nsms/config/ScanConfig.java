/*
 * Copyright (c) 2020, Newstrength. All rights reserved.
 */
package cn.newstrength.nsms.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
/**
 * 添加其他允许Spring扫描的package
 * @author Xd
 *
 */
@Configuration
@ComponentScan(
		{ 
			"cn.newstrength.wcms.sample",
			"cn.newstrength.wcms.tablib",
			"cn.newstrength.wcms.site",
			"cn.newstrength.wcms.seo",
			"cn.newstrength.wcms.info",
			"cn.newstrength.wcms.template",
			"cn.newstrength.wcms.d2s",
			"cn.newstrength.wcms.field",
			"cn.newstrength.wcms.channel"
			}
		)
public class ScanConfig {

}
