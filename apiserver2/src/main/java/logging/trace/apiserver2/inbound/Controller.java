package logging.trace.apiserver2.inbound;

import jakarta.servlet.http.HttpServletRequest;
import logging.trace.apiserver2.configs.LogTraceConfigs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("")
@Slf4j
@RequiredArgsConstructor
public class Controller {

	private final LogTraceConfigs logTraceConfigs;
	@GetMapping("/hello")
	public String hello(HttpServletRequest request) {
		log.info("Hello, World!");
//		webClient.get().uri("http://localhost:12002/hello").retrieve().bodyToMono(String.class).block();
		return request.getHeader(logTraceConfigs.getKey());
	}
}
