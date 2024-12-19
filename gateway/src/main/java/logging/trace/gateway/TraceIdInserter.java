package logging.trace.gateway;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
									log.info("key : " + config.getKey());
									log.info("Generated trace id: {}", uuid);// exchange를 mutate()를 통해 수정
									ServerWebExchange mutatedExchange = exchange.mutate()
													.request(r -> r.header(config.getKey(), uuid.toString()))
													.build();
									return chain.filter(mutatedExchange)
													.then(Mono.fromRunnable(() -> {
														log.info("Clearing trace id: {}", uuid);
													}));
								});
		}
}
