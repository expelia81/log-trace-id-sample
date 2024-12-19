package com.example.demo.inbound;

import com.example.demo.configs.LogTraceConfigs;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@Order(Integer.MIN_VALUE)
@RequiredArgsConstructor
public class ContextFilter implements Filter {

	private final LogTraceConfigs logTraceConfigs;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;

		String traceId = request.getHeader(logTraceConfigs.getKey());

		if (traceId != null && !traceId.isEmpty()) {
			ThreadContext.put(logTraceConfigs.getKey(), traceId);
		} else {
			ThreadContext.put(logTraceConfigs.getKey(), logTraceConfigs.getDefaultValue());
		}

		filterChain.doFilter(servletRequest, servletResponse);

		ThreadContext.clearAll();
	}
}
