package pl.lotto.domain.winningnumbergenerator;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.winningnumbergenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class WinningNumbersGenerableFacadeTest {

    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    WinningNumbersRepository repository = new WinningNumbersRepositoryTestImpl();
    RandomNumbersGenerable randomNumbersGenerator = new RandomNumbersGenerator();
    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade =
            new WinningNumbersGeneratorConfiguration().createForTest(numberReceiverFacade,repository, randomNumbersGenerator);

    @Test
    public void shouldGenerateSixNumbers() {
        //given
        //when
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        Set<Integer> winningNumbers = winningNumbersGeneratorFacade.generateWinningNumbers().winningNumbers();
        //then
        assertEquals(6, winningNumbers.size());
    }

//    @Test
//    public void shouldGenerateNumbersInAppropriateRange() {
//        //given
//        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
//        //when
//        Set<Integer> winningNumbers = winningNumberGeneratorFacade.generateWinningNumbers().winningNumbers();
//        //then
//        assertTrue(winningNumbers.stream().allMatch((number) -> number >= 1 && number <= 99));
//    }
//  --------- TEST shouldGenerateNumbersInAppropriateRange and shouldThrowExceptionWhenGeneratedWinningNumbersAreOutOfRange test the same case -------------
    @Test
    public void shouldThrowExceptionWhenGeneratedWinningNumbersAreOutOfRange(){
        //given
        RandomNumbersGeneratorTestImpl winningNumberGeneratorTest = new RandomNumbersGeneratorTestImpl();
        WinningNumbersGeneratorFacade facadeForTest = new WinningNumbersGeneratorConfiguration().createForTest(numberReceiverFacade,repository,winningNumberGeneratorTest);
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