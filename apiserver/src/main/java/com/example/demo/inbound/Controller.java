package com.example.demo.inbound;

import com.example.demo.configs.Audit;
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

	@PostConstruct
	public void init() {
		log.info(String.valueOf(internalWebClient.hashCode()));
		log.info(String.valueOf(externalWebClient.hashCode()));
	}
	@GetMapping("/hello")
	public ResponseEntity<String> hello() {

		Audit.info(Audit.Target.from("VM", "dy.choi-123-341sad-1231sdas1", "최두영-VM"), Audit.Action.from("hello world"), Audit.Status.from("running"), "first api server hello world api called!");

		log.info("첫 번째 서버 API 테스트 중입니다.");
		return internalWebClient.get()
						.uri("http://localhost:12002/hello")
//						.header(logTraceConfigs.getKey(), ThreadContext.get(logTraceConfigs.getKey()))
						.retrieve().bodyToMono(String.class)
						.map(s -> ResponseEntity.status(201).body(s))
						.block();
	}
}
