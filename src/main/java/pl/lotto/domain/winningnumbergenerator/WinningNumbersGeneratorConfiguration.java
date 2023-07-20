package pl.lotto.domain.winningnumbergenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;


@Configuration
public class WinningNumbersGeneratorConfiguration {

    @Bean
    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade(
            WinningNumbersRepository winningNumbersRepository,
            NumberReceiverFacade numberReceiverFacade,
            RandomNumbersGenerable randomNumbersGenerator,
            WinningNumbersGeneratorFacadeConfigurationProperties properties
    ) {
        WinningNumbersValidator winningNumbersValidator = new WinningNumbersValidator();
        return new WinningNumbersGeneratorFacade(
                randomNumbersGenerator, numberReceiverFacade, winningNumbersValidator, winningNumbersRepository, properties);
    }


    public WinningNumbersGeneratorFacade createForTest(
            NumberReceiverFacade numberReceiverFacade,
            WinningNumbersRepository repository,
            RandomNumbersGenerable randomNumbersGenerator
    ) {
        WinningNumbersGeneratorFacadeConfigurationProperties properties = WinningNumbersGeneratorFacadeConfigurationProperties.builder()
                .upperBand(99)
                .lowerBand(1)
                .count(6)
                .build();

        return winningNumbersGeneratorFacade(
                repository,
                numberReceiverFacade,
                randomNumbersGenerator,
                properties
        );
    }
}
