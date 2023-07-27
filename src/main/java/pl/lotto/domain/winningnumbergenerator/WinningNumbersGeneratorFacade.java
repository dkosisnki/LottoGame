package pl.lotto.domain.winningnumbergenerator;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.winningnumbergenerator.dto.SixRandomNumbersDto;
import pl.lotto.domain.winningnumbergenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {

    private final RandomNumbersGenerable randomNumberGenerator;
    private final NumberReceiverFacade numberReceiverFacade;
    private final WinningNumbersValidator winningNumbersValidator;
    private final WinningNumbersRepository repository;
    private final WinningNumbersGeneratorFacadeConfigurationProperties properties;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime nextDrawDate = numberReceiverFacade.retrieveNextDrawDate();

        SixRandomNumbersDto sixRandomNumbersDto = randomNumberGenerator.generateSixRandomNumbers(properties.count(), properties.lowerBand(), properties.upperBand());
        Set<Integer> randomNumbers = sixRandomNumbersDto.numbers();

        winningNumbersValidator.validate(randomNumbers);

        WinningNumbers winningNumbersDocument = WinningNumbers.builder()
                .numbers(randomNumbers)
                .drawDate(nextDrawDate)
                .build();

        WinningNumbers savedWinningNumbers = repository.save(winningNumbersDocument);

        return WinningNumbersDto.builder()
                .winningNumbers(savedWinningNumbers.numbers())
                .drawDate(savedWinningNumbers.drawDate())
                .build();
    }

    public WinningNumbersDto retrieveWonNumbersForDate(LocalDateTime date){
        WinningNumbers winningNumbers = repository.findWinningNumbersByDrawDate(date)
                .orElseThrow(
                        () -> new WinningNumbersNotFoundException("Not found numbers for date " + date));

        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbers.numbers())
                .drawDate(winningNumbers.drawDate())
                .build();
    }

    public boolean areWinningsNumberGeneratedByDate() {
        LocalDateTime drawDate = numberReceiverFacade.retrieveNextDrawDate();
        return repository.existsByDrawDate(drawDate);
    }
}
