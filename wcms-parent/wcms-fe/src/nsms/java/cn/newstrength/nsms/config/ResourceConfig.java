/*
 * Copyright (c) 2020, NewStrength. All rights reserved.
 */

package cn.newstrength.nsms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 静态资源外部配置类
 *
 * @author kyrie 2021/3/19 5:41 下午
 * @since jdk1.8
 */
@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Value("${spring.resources.static-locations}")
    private String resourceLocations;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/**")
                .addResourceLocations(resourceLocations);
    }
}
