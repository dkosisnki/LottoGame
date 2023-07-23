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

}
