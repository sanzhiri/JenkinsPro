/*
 * Copyright (c) 2020, Newstrength. All rights reserved.
 */
package cn.newstrength.nsms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 * NSMS 应用启动主入口，此类除非必须请不要做修改
 * @author Xd
 *
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableScheduling
public class Application {
	public static void main(String[] args) {
		SpringApplication sa = new SpringApplication(Application.class);
		sa.addListeners(new ApplicationPidFileWriter());
		sa.run(args);
	}
}
