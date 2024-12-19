package com.example.demo.configs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "logging.trace")
@Configuration
@ToString
@Getter
@Setter
public class LogTraceConfigs {

	private String key = "X-TRACE-ID";
	private String defaultValue = "TRACE_ID_NOT_FOUND";
	private String systemDefault = "SYSTEM";
}
