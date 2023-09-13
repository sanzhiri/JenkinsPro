/*
 * Copyright (c) 2020, NewStrength. All rights reserved.
 */

package cn.newstrength.nsms.config;

import io.undertow.UndertowOptions;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 服务配置类
 * <p>服务配置主类,本类提供服务的主要配置</p>
 * 目前已经加入的配置有
 * <ul>
 *     <li>ConfigurableServletWebServerFactory</li>
 * </ul>
 *
 * @author kyrie 2020/10/9 11:47 上午
 * @since jdk1.8
 */
@Configuration
public class ServerConfig {

    /**
     * 配置Undertow webServer。
     * 允许http请求中包含特殊字符<i>|{}[]</i>等
     * @return UndertowServletWebServerFactory
     */
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();

        factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ALLOW_UNESCAPED_CHARACTERS_IN_URL, Boolean.TRUE)); //url配置
        factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ALLOW_ENCODED_SLASH,Boolean.TRUE));
        return factory;
    }

    /**
     * 配置Tomcat webServer。
     * 允许http请求中包含特殊字符<i>|{}[]</i>等
     * @return TomcatServletWebServerFactory

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                connector.setProperty("relaxedQueryChars", "|{}[]");
            }
        });
        return factory;
    }*/
}
