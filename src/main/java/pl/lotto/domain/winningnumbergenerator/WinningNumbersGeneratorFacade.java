package pl.lotto.domain.winningnumbergenerator;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.winningnumbergenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {

    private final RandomNumbersGenerable randomNumberGenerator;
    private final NumberReceiverFacade numberReceiverFacade;
    private final WinningNumbersValidator winningNumbersValidator;
    private final WinningNumbersRepository repository;
    private final HashGenerable hashGenerator;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime nextDrawDate = numberReceiverFacade.retrieveNextDrawDate();

        Set<Integer> randomNumbers = randomNumberGenerator.generateWinningNumbers();

        winningNumbersValidator.validate(randomNumbers);

        //TODO rather id won't be useful
        String id = hashGenerator.getHash();

        WinningNumbers winningNumbers = WinningNumbers.builder()
                .id(id)
                .numbers(randomNumbers)
                .drawDate(nextDrawDate)
                .build();

        repository.save(winningNumbers);

        return WinningNumbersDto.builder()
                .winningNumbers(randomNumbers)
                .drawDate(nextDrawDate)
                .build();
    }

    public WinningNumbersDto retrieveWonNumbersForDate(LocalDateTime date){
        WinningNumbers winningNumbers = repository.findWonNumbersForDate(date)
                .orElseThrow(
                        () -> new WinningNumbersNotFoundException("Not found numbers for date " + date));

        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbers.numbers())
                .drawDate(winningNumbers.drawDate())
                .build();
    }
}
