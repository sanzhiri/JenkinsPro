/*
 * Copyright (c) 2019, NewStrength. All rights reserved.
 */
package cn.newstrength.nsms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * NSMS 应用启动主入口，此类除非必须请不要做修改
 * 
 * @author Xd
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Application {
	public static void main(String[] args) {
		SpringApplication sa = new SpringApplication(Application.class);
		sa.addListeners(new ApplicationPidFileWriter());
		sa.run(args);
	}
}
