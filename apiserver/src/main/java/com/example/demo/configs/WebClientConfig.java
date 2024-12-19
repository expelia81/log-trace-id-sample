package com.example.demo.configs;

import com.example.demo.configs.LogTraceConfigs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebClientConfig {

	private final LogTraceConfigs logTraceConfigs;

	@PostConstruct
	public void init() {
		log.info("logTraceConfigs: {}", logTraceConfigs);
	}
	@Bean
	public WebClient webClient() {
		return WebClient.builder()
						.filter((request, next) -> {
							log.info("output trace id : {} ", ThreadContext.get(logTraceConfigs.getKey()));
							ClientRequest clientRequest = ClientRequest.from(request)
											.header(logTraceConfigs.getKey(), ThreadContext.get(logTraceConfigs.getKey()))
											.build();
							return next.exchange(clientRequest);
						})
//						.apply(builder -> {
//							log.info("input trace id : {} ", ThreadContext.get(logTraceConfigs.getKey()));
//							builder.defaultHeaders(httpHeaders -> {
//								httpHeaders.add(logTraceConfigs.getKey(), ThreadContext.get(logTraceConfigs.getKey()));
//							});
//						})
						.build()
						;
	}
}
