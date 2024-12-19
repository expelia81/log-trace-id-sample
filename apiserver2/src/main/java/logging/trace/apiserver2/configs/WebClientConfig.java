package logging.trace.apiserver2.configs;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
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
						.apply(builder -> builder.defaultHeader(logTraceConfigs.getKey(), ThreadContext.get(logTraceConfigs.getKey())))
						.build()
						;
	}
}
