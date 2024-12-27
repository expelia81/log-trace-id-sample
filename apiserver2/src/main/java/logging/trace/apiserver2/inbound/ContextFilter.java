package logging.trace.apiserver2.inbound;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import logging.trace.apiserver2.configs.LogTraceConfigs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@Order(Integer.MIN_VALUE)
@RequiredArgsConstructor
public class ContextFilter implements Filter {


	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;

		String actorType = Optional.ofNullable(request.getHeader(LogTraceConfigs.actorType)).orElse("user");
		String actorId = Optional.ofNullable(request.getHeader(LogTraceConfigs.actorId)).orElse("");
		String actorName = Optional.ofNullable(request.getHeader(LogTraceConfigs.actorName)).orElse("");
		String actorIp = Optional.ofNullable(request.getHeader(LogTraceConfigs.actorIp)).orElse(request.getRemoteAddr());
		String traceId = Optional.ofNullable(request.getHeader(LogTraceConfigs.trace)).orElse(LogTraceConfigs.traceDefault);
		int order = Integer.parseInt(Optional.ofNullable(request.getHeader(LogTraceConfigs.order)).orElse("0"))+1;
//		String span = Optional.ofNullable(request.getHeader(LogTraceConfigs.getSpan())).orElse("");
		ThreadContext.put(LogTraceConfigs.trace, traceId);
		ThreadContext.put(LogTraceConfigs.order, String.valueOf(order));
		ThreadContext.put(LogTraceConfigs.span, UUID.randomUUID().toString().replace("-", ""));
		ThreadContext.put(LogTraceConfigs.actorType, actorType);
		ThreadContext.put(LogTraceConfigs.actorId, actorId);
		ThreadContext.put(LogTraceConfigs.actorName, actorName);
		ThreadContext.put(LogTraceConfigs.actorIp, actorIp);


		filterChain.doFilter(servletRequest, servletResponse);

		ThreadContext.clearAll();
	}
}
