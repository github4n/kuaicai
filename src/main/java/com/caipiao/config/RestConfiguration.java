package com.xiaoniu.thirdplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfiguration {

	@Bean
	public RestOperations restOperations() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		// 设置连接超时，单位：毫秒
		requestFactory.setConnectTimeout(5000);
		// 设置读取超时，单位：毫秒
		requestFactory.setReadTimeout(5000);

		RestTemplate restTemplate = new RestTemplate(requestFactory);

		return restTemplate;
	}


	@Bean
	public RestOperations restHttpsOperations() {
		HttpsClientRequestFactory requestFactory = new HttpsClientRequestFactory();
		// 设置连接超时，单位：毫秒
		requestFactory.setConnectTimeout(5000);
		// 设置读取超时，单位：毫秒
		requestFactory.setReadTimeout(5000);

		RestTemplate restTemplate = new RestTemplate(requestFactory);

		return restTemplate;
	}

}
