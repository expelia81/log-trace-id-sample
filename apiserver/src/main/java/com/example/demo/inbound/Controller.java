package com.example.demo.inbound;

import com.example.demo.configs.LogTraceConfigs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("")
@Slf4j
@RequiredArgsConstructor
public class Controller {

	@Qualifier("internalWebClient")
	@Autowired
	private WebClient internalWebClient;
	@Qualifier("externalWebClient")
	@Autowired
	private WebClient externalWebClient;
	private final LogTraceConfigs logTraceConfigs;

	@PostConstruct
	public void init() {
		log.info(String.valueOf(internalWebClient.hashCode()));
		log.info(String.valueOf(externalWebClient.hashCode()));
	}
	@GetMapping("/hello")
	public ResponseEntity<String> hello() {
		log.info("Hello, World!");
		return internalWebClient.get()
						.uri("http://localhost:12002/hello")
//						.header(logTraceConfigs.getKey(), ThreadContext.get(logTraceConfigs.getKey()))
						.retrieve().bodyToMono(String.class)
						.map(s -> ResponseEntity.status(201).body(s))
						.block();
	}
}
