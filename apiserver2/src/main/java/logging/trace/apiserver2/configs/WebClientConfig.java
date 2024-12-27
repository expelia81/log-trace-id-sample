package logging.trace.apiserver2.configs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebClientConfig {


	@Bean("internalWebClient")
	@Primary
	public WebClient webClient() {
		return WebClient.builder()
						.filter((request, next) -> {
							log.info("output trace id : {} ", ThreadContext.get(LogTraceConfigs.trace));

							ClientRequest clientRequest = LogTraceConfigs.createClientRequest(request);
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
	@Bean("externalWebClient")
	public WebClient externalWebClient() {
		return WebClient.builder()
						.build();
	}
}
