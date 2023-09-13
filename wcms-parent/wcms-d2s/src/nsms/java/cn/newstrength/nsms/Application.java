package cn.newstrength.nsms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Auther: zrf
 * @Date: 2020/12/10 11:09
 * @Description: D2S动转静服务启动类入口
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableAsync
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}