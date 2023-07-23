package pl.lotto.domain.winningnumbergenerator;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.winningnumbergenerator.dto.SixRandomNumbersDto;
import pl.lotto.domain.winningnumbergenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class WinningNumbersGenerableFacadeTest {

    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    WinningNumbersRepository repository = new WinningNumbersRepositoryTestImpl();
    RandomNumbersGenerable randomNumbersGenerator = new RandomNumbersGeneratorTestImpl();

    WinningNumbersGeneratorFacadeConfigurationProperties properties = WinningNumbersGeneratorFacadeConfigurationProperties.builder()
            .upperBand(99)
            .lowerBand(1)
            .count(6)
            .build();
    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade =
            new WinningNumbersGeneratorConfiguration().winningNumbersGeneratorFacade(
                    repository,numberReceiverFacade,randomNumbersGenerator,properties
            );

    @Test
    public void shouldGenerateSixNumbers() {
        //given
        //when
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        Set<Integer> winningNumbers = winningNumbersGeneratorFacade.generateWinningNumbers().winningNumbers();
        //then
        assertEquals(6, winningNumbers.size());
    }

    @Test
    public void shouldGenerateNumbersInAppropriateRange() {
        //given
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //when
        Set<Integer> winningNumbers = winningNumbersGeneratorFacade.generateWinningNumbers().winningNumbers();
        //then
        assertTrue(winningNumbers.stream().allMatch((number) -> number >= 1 && number <= 99));
    }

    @Test
    public void shouldThrowExceptionWhenGeneratedWinningNumbersAreOutOfRange(){
        //given
        RandomNumbersGenerable winningNumberGeneratorTest = (count,lowerBand,upperBand) -> SixRandomNumbersDto.builder()
                .numbers(Set.of(1,3,5,6,100))
                .build();
        WinningNumbersGeneratorFacade facadeForTest = new WinningNumbersGeneratorConfiguration().winningNumbersGeneratorFacade(
                repository,numberReceiverFacade,winningNumberGeneratorTest,properties
        );
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //when
        //then
        IllegalStateException exception = assertThrows(IllegalStateException.class, facadeForTest::generateWinningNumbers);
        assertEquals("Number out of range!", exception.getMessage());
    }

    @Test
    public void shouldThrownNotFoundExceptionWhenThereIsNoNumberForGivenDate(){
        //given
        LocalDateTime drawDateWithNumbers = LocalDateTime.of(2023, 6, 17, 12, 0, 0);
        LocalDateTime drawDateWithoutNumbers = drawDateWithNumbers.plusWeeks(1);
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDateWithNumbers);
        winningNumbersGeneratorFacade.generateWinningNumbers();
        //when
        //then
        WinningNumbersNotFoundException notFoundException =
                assertThrows(WinningNumbersNotFoundException.class,
                        () -> winningNumbersGeneratorFacade.retrieveWonNumbersForDate(drawDateWithoutNumbers));
        assertEquals("Not found numbers for date " + drawDateWithoutNumbers, notFoundException.getMessage());
    }

    @Test
    public void shouldReturnNumbersForDrawDataCorrectly(){
        //given
        LocalDateTime drawDate = LocalDateTime.of(2023, 6, 17, 12, 0, 0);
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        winningNumbersGeneratorFacade.generateWinningNumbers();
        //when
        WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.retrieveWonNumbersForDate(drawDate);
        //then
        assertEquals(drawDate,winningNumbersDto.drawDate());
    }
}