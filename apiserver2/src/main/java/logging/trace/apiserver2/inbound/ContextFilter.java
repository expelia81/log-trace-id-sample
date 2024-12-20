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

@Slf4j
@Component
@Order(Integer.MIN_VALUE)
@RequiredArgsConstructor
public class ContextFilter implements Filter {

	private final LogTraceConfigs logTraceConfigs;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;

		String traceId = Optional.ofNullable(request.getHeader(logTraceConfigs.getKey())).orElse(logTraceConfigs.getDefaultValue());
		String userId = Optional.ofNullable(request.getHeader(logTraceConfigs.getUserId())).orElse("");
		String userName = Optional.ofNullable(request.getHeader(logTraceConfigs.getUserName())).orElse("");

		ThreadContext.put(logTraceConfigs.getKey(), traceId);
		ThreadContext.put(logTraceConfigs.getUserId(), userId);
		ThreadContext.put(logTraceConfigs.getUserName(), userName);

		filterChain.doFilter(servletRequest, servletResponse);

		ThreadContext.clearAll();
	}
}
