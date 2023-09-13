package cn.newstrength.nsms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import cn.newstrength.wtdf.plugin.util.SpringUtils;

/**
 * SpringBoot Application
 * 
 * @author Xd
 *
 **修改启动类，继承 SpringBootServletInitializer 并重写 configure 方法
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Application  extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public SpringUtils springUtils() {
		return new SpringUtils();
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(this.getClass()); //super.configure(builder);
	}
}

