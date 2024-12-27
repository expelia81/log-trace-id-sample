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
	}

		@Override
		public org.springframework.cloud.gateway.filter.GatewayFilter apply(Config config) {
				return (exchange, chain) -> Mono.fromCallable(() -> UUID.randomUUID().toString().replace("-", ""))
								.flatMap(uuid -> {
//									log.info("key : " + config.getKey());
//									log.info("Generated trace id: {}", uuid);// exchange를 mutate()를 통해 수정
									String userId = exchange.getRequest().getHeaders().getFirst("x-user-id") != null ? exchange.getRequest().getHeaders().getFirst("x-user-id") : "not found";
									String userName = exchange.getRequest().getHeaders().getFirst("x-user-name") != null ? exchange.getRequest().getHeaders().getFirst("x-user-name") : "unnamed";

									String clientIP = getRemoteIp(exchange);
									ServerWebExchange mutated = exchange.mutate().request(exchange.getRequest().mutate()
																	.header(LogTraceConfigs.trace, uuid)
																	.header(LogTraceConfigs.actionType, "request")
																	.header(LogTraceConfigs.actorType, "user")
																	.header(LogTraceConfigs.actorId, userId)
																	.header(LogTraceConfigs.actorName, userName)
																	.header(LogTraceConfigs.actorIp, clientIP)
																	.header(LogTraceConfigs.order, "0")
													.build()).build();
									String path = exchange.getRequest().getPath().value();
									return chain.filter(mutated)
													.doOnSuccess(aVoid -> {
														ThreadContext.put(LogTraceConfigs.trace, uuid);
														ThreadContext.put(LogTraceConfigs.actionType, "response");
														ThreadContext.put(LogTraceConfigs.actorType, "user");
														ThreadContext.put(LogTraceConfigs.actorId, userId);
														ThreadContext.put(LogTraceConfigs.actorName, userName);
														ThreadContext.put(LogTraceConfigs.actorIp, clientIP);
														ThreadContext.put(LogTraceConfigs.order, "0");


														// 일반 로그
														log.info("Request path : " + path);

														// Audit 로그
														Audit.info("Request path : " + path);


														ThreadContext.clearAll();
													});
								})



								;
		}


	public static String getRemoteIp(ServerWebExchange exchange) {
		try {
			return exchange.getRequest().getHeaders().getFirst("X-Forwarded-For").split(",")[0].trim();
		} catch (NullPointerException e) {
			return exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
		}
	}
}
