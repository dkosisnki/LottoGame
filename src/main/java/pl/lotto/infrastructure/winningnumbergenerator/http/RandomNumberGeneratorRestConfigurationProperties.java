package pl.lotto.infrastructure.winningnumbergenerator.http;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lotto.http.client.config")
@Builder
public record RandomNumberGeneratorRestConfigurationProperties(
        String uri,
        int port,
        long connectionTimeout,
        long readTimeout
) {
}
