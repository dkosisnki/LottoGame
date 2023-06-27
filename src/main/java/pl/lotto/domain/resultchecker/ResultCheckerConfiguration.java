package pl.lotto.domain.resultchecker;

import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.winningnumbergenerator.WinningNumbersGeneratorFacade;

public class ResultCheckerConfiguration {

    ResultCheckerFacade createForTest(
            NumberReceiverFacade numberReceiverFacade,
            WinningNumbersGeneratorFacade winningNumbersGeneratorFacade,
            PlayerRepository repository
    ){
        WinnersRetriever winnersRetriever = new WinnersRetriever();
        return new ResultCheckerFacade(numberReceiverFacade,winningNumbersGeneratorFacade,winnersRetriever,repository);
    }

}
