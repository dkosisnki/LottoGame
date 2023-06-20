package pl.lotto.domain.winningnumbergenerator;

import pl.lotto.domain.numberreceiver.NumberReceiverFacade;


public class WinningNumbersGeneratorConfiguration {

   public WinningNumbersGeneratorFacade createForTest(NumberReceiverFacade numberReceiverFacade,WinningNumbersRepository repository, RandomNumbersGenerable randomNumbersGenerator) {
        HashGenerable hashGenerator = new HashGenerator();
        WinningNumbersValidator winningNumbersValidator = new WinningNumbersValidator();

        return new WinningNumbersGeneratorFacade(
                                randomNumbersGenerator,
                                numberReceiverFacade,
                                winningNumbersValidator,
                                repository,
                                hashGenerator
        );
    }
}
