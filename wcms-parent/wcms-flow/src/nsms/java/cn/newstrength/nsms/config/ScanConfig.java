/*
 * Copyright (c) 2020, Newstrength. All rights reserved.
 */
package cn.newstrength.nsms.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
/**
 * 添加其他允许Spring扫描的package
 * @author Xd
 *
 */
@Configuration
@ImportResource(locations = { "classpath:config/spring/*.xml" })
@ComponentScan(
		{ 
			"cn.newstrength.flowable",
			"cn.newstrength.wcms.flow"
			}
		)
public class ScanConfig {

}
