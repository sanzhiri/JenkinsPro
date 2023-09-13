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
			"cn.newstrength.wcms.sample.controller", 
			"cn.newstrength.wcms.service", 
			"cn.newstrength.wcms.core", 
			"cn.newstrength.wcms.core.tag.service",
			"cn.newstrength.widm.service" ,
			"cn.newstrength.wcms.sysadmin",
			"cn.newstrength.widm.flow.service"
			}
		)
public class ScanConfig {

}
