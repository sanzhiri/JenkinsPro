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
@ComponentScan({ "cn.newstrength.wcms"})
public class ScanConfig {

}
