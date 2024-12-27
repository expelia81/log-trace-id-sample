package logging.trace.apiserver2.inbound;

import jakarta.servlet.http.HttpServletRequest;
import logging.trace.apiserver2.configs.Audit;
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
	@GetMapping("/hello")
	public String hello(HttpServletRequest request) {
		log.info("테스트 중입니다.");
		Audit.info(Audit.Target.from("VM", "dy.choi-123-341sad-1231sdas1", "최두영-VM"), Audit.Action.from("hello world"), Audit.Status.from("running"), "hello world api called!");
//		webClient.get().uri("http://localhost:12002/hello").retrieve().bodyToMono(String.class).block();
		return request.getHeader(LogTraceConfigs.trace);
	}
}
