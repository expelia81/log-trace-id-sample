package logging.trace.apiserver2.configs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;

public class LogTraceConfigs {

	public static String trace = "X-TRACE-ID";
	public static String span = "X-SPAN-ID";
	public static String traceDefault = "system";
	public static String order = "X-ORDER";


	public static String actorType = "X-ACTOR-TYPE";
	public static String actorId = "X-ACTOR-ID";
	public static String actorName = "X-ACTOR-NAME";
	public static String actorIp = "X-ACTOR-IP";

	public static String actionType = "X-ACTION-TYPE";

	public static String targetType = "X-TARGET-TYPE";
	public static String targetId = "X-TARGET-ID";
	public static String targetName = "X-TARGET-NAME";

	public static String status = "X-STATUS";

	public static ClientRequest createClientRequest(ClientRequest request) {
		ClientRequest clientRequest = ClientRequest.from(request)
						.header(trace, ThreadContext.get(trace))
						.header(span, ThreadContext.get(span))
						.header(order, ThreadContext.get(order))
						.header(actorType, ThreadContext.get(actorType))
						.header(actorId, ThreadContext.get(actorId))
						.header(actorName, ThreadContext.get(actorName))
						.header(actorIp, ThreadContext.get(actorIp))
						.build();

		return clientRequest;
	}
}