package pl.lotto.infrastructure.cache;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.redis")
@Builder
public record RedisProperties(
        String host,
        int port
) {
}
