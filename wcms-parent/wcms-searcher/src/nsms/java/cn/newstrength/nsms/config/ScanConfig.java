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
			"cn.newstrength.wcms.service", 
			"cn.newstrength.wcms.info.api.service",
			"cn.newstrength.wcms.job",
			"cn.newstrength.wcms.d2s",
			}
		)
public class ScanConfig {

}
