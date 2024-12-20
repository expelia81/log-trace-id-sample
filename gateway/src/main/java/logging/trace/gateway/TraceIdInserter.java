package logging.trace.gateway;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
@Order(1)
public class TraceIdInserter extends AbstractGatewayFilterFactory<TraceIdInserter.Config> {

		public TraceIdInserter() {
				super(Config.class);
		}

		@Getter
		@Setter
		public static class Config {
			private String key = "X-Trace-Id";
		}

		@Override
		public org.springframework.cloud.gateway.filter.GatewayFilter apply(Config config) {
				return (exchange, chain) -> Mono.fromCallable(() -> UUID.randomUUID())
								.flatMap(uuid -> {
//									log.info("key : " + config.getKey());
//									log.info("Generated trace id: {}", uuid);// exchange를 mutate()를 통해 수정
									String userId = exchange.getRequest().getHeaders().getFirst("x-user-id");
									if (userId == null) {
										userId = "not-found";
									}
									String userName = exchange.getRequest().getHeaders().getFirst("x-user-name");
									if (userName == null) {
										userName = "unnamed";
									}
									ServerWebExchange mutated = exchange.mutate().request(exchange.getRequest().mutate()
													.header(config.getKey(), uuid.toString())
													.header("X-USER-ID", userId)
													.header("x-user-name", userName)
													.build()).build();
									String path = exchange.getRequest().getPath().value();
									return chain.filter(mutated)
													.doOnSuccess(aVoid -> {
														ThreadContext.put(config.getKey(), uuid.toString());
														ThreadContext.put("X-USER-ID", mutated.getRequest().getHeaders().getFirst("X-USER-ID"));
														ThreadContext.put("X-USER-NAME", mutated.getRequest().getHeaders().getFirst("X-user-name"));
														log.info("Request path in context: {}", path);
														ThreadContext.clearAll();
													});
								})



								;
		}
}
