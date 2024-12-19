package logging.trace.gateway;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "logging.trace")
@Configuration
@ToString
@Getter
@Setter
public class LogTraceConfigs {


}
