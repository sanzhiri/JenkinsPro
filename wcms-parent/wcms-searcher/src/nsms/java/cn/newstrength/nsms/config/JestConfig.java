package cn.newstrength.nsms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

@Configuration
public class JestConfig {
	@Value("${spring.elasticsearch.jest.uris}")
	private String uris;
	@Bean
	public JestClient getJestClient() {
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(
				new HttpClientConfig.Builder(uris).multiThreaded(true).build());
		return factory.getObject();
	}
}
