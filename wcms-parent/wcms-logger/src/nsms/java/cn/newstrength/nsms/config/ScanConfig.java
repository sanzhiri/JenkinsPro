package cn.newstrength.nsms.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import cn.newstrength.logger.service.LoggerService;
import cn.newstrength.logger.service.impl.RedisToDMServiceImpl;
import cn.newstrength.logger.wcms.service.QueryService;
import cn.newstrength.logger.wcms.service.impl.WCMSServiceImpl;

import cn.newstrength.logger.service.impl.RedisToDMServiceImpl;
@Configuration
@MapperScan({"cn.newstrength.logger.mapper","cn.newstrength.nsms.mybatis"})
@ComponentScan({"cn.newstrength.logger"})
public class ScanConfig {

	@Bean
	public LoggerService loggerService() {
		return new RedisToDMServiceImpl();
	}
	
	@Bean
	public QueryService wcmsService() {
		return new WCMSServiceImpl();
	}

}
