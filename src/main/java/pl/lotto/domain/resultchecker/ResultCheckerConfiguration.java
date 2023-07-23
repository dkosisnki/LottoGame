package pl.lotto.domain.resultchecker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.winningnumbergenerator.WinningNumbersGeneratorFacade;

@Configuration
public class ResultCheckerConfiguration {

    @Bean
    ResultCheckerFacade resultCheckerFacade(
            NumberReceiverFacade numberReceiverFacade,
            WinningNumbersGeneratorFacade winningNumbersGeneratorFacade,
            PlayerRepository repository
    ){
        WinnersRetriever winnersRetriever = new WinnersRetriever();
        return new ResultCheckerFacade(numberReceiverFacade,winningNumbersGeneratorFacade,winnersRetriever,repository);
    }

}
