package com.example.demo.inbound;

import com.example.demo.configs.LogTraceConfigs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("")
@Slf4j
@RequiredArgsConstructor
public class Controller {

	private final WebClient webClient;
	private final LogTraceConfigs logTraceConfigs;

	@GetMapping("/hello")
	public String hello() {
		log.info("Hello, World!");
		return webClient.get()
						.uri("http://localhost:12002/hello")
//						.header(logTraceConfigs.getKey(), ThreadContext.get(logTraceConfigs.getKey()))
						.retrieve().bodyToMono(String.class).block();
	}
}
