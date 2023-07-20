package pl.lotto.domain.winningnumbergenerator;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lotto.number-generator.facade")
@Builder
public record WinningNumbersGeneratorFacadeConfigurationProperties(int count,int lowerBand, int upperBand) {
}
