package pl.lotto.domain.resultchecker;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.PlayerDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;
import pl.lotto.domain.winningnumbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.winningnumbergenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultCheckerFacadeTest {

    private final NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = mock(WinningNumbersGeneratorFacade.class);
    private final PlayerRepositoryTestImpl repository = new PlayerRepositoryTestImpl();

    @Test
    public void shouldReturnFailedMessageWhenWinningNumbersAreNull(){
        //given
        when(winningNumbersGeneratorFacade.generateWinningNumbers())
                .thenReturn(WinningNumbersDto.builder()
                        .winningNumbers(null)
                        .build());

        ResultCheckerFacade resultChecker
                = new ResultCheckerConfiguration().createForTest(numberReceiverFacade, winningNumbersGeneratorFacade, repository);
        //when
        PlayerDto playerDto = resultChecker.checkResult();
        //then
        assertEquals("Winners failed to retrieve",playerDto.message());
    }

    @Test
    public void shouldReturnFailedMessageWhenWinningNumbersAreEmpty(){
        //given
        when(winningNumbersGeneratorFacade.generateWinningNumbers())
                .thenReturn(WinningNumbersDto.builder()
                        .winningNumbers(Set.of())
                        .build());

        ResultCheckerFacade resultChecker
                = new ResultCheckerConfiguration().createForTest(numberReceiverFacade, winningNumbersGeneratorFacade, repository);
        //when
        PlayerDto playerDto = resultChecker.checkResult();
        //then
        assertEquals("Winners failed to retrieve",playerDto.message());
    }

    @Test
    public void shouldReturnCorrectMessageAndResult(){
        //given
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(List.of(
                TicketDto.builder()
                        .hash("hash1")
                        .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                        .numbers(Set.of(1,2,3,4,5,6))
                        .build(),
                TicketDto.builder()
                        .hash("hash2")
                        .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                        .numbers(Set.of(4,5,6,7,8,9))
                        .build(),
                TicketDto.builder()
                        .hash("hash3")
                        .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                        .numbers(Set.of(13,22,34,41,55,69))
                        .build()
        ));

        when(winningNumbersGeneratorFacade.generateWinningNumbers())
                .thenReturn(WinningNumbersDto.builder()
                        .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                        .winningNumbers(Set.of(1,2,3,4,5,6))
                        .build());

        ResultCheckerFacade resultChecker
                = new ResultCheckerConfiguration().createForTest(numberReceiverFacade, winningNumbersGeneratorFacade, repository);

        //when
        PlayerDto playerDto = resultChecker.checkResult();

        //then
        assertEquals("Winners success to retrieve",playerDto.message());
        assertEquals(3,playerDto.results().size());
    }

    @Test
    public void shouldFindPlayerByHashIdCorrectly(){
        //given
        String hashIdToFind = "hash2";
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(List.of(
                TicketDto.builder()
                        .hash("hash1")
                        .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                        .numbers(Set.of(1,2,3,4,5,6))
                        .build(),
                TicketDto.builder()
                        .hash("hash2")
                        .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                        .numbers(Set.of(4,5,6,7,8,9))
                        .build(),
                TicketDto.builder()
                        .hash("hash3")
                        .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                        .numbers(Set.of(13,22,34,41,55,69))
                        .build()
        ));

        when(winningNumbersGeneratorFacade.generateWinningNumbers())
                .thenReturn(WinningNumbersDto.builder()
                        .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                        .winningNumbers(Set.of(1,2,3,4,5,6))
                        .build());

        ResultCheckerFacade resultChecker
                = new ResultCheckerConfiguration().createForTest(numberReceiverFacade, winningNumbersGeneratorFacade, repository);
        resultChecker.checkResult();

        //when
        ResultDto result = resultChecker.findPlayerByHash(hashIdToFind);

        //then
        assertEquals(hashIdToFind,result.hash());
    }
    @Test
    public void shouldThrowResultCheckerNotFoundExceptionWhenInRepositoryIsNoPlayerWithGivenHashId(){
        //given
        String hashIdToFind = "hash5";
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(List.of(
                TicketDto.builder()
                        .hash("hash1")
                        .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                        .numbers(Set.of(1,2,3,4,5,6))
                        .build(),
                TicketDto.builder()
                        .hash("hash2")
                        .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                        .numbers(Set.of(4,5,6,7,8,9))
                        .build(),
                TicketDto.builder()
                        .hash("hash3")
                        .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                        .numbers(Set.of(13,22,34,41,55,69))
                        .build()
        ));

        when(winningNumbersGeneratorFacade.generateWinningNumbers())
                .thenReturn(WinningNumbersDto.builder()
                        .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                        .winningNumbers(Set.of(1,2,3,4,5,6))
                        .build());

        ResultCheckerFacade resultChecker
                = new ResultCheckerConfiguration().createForTest(numberReceiverFacade, winningNumbersGeneratorFacade, repository);
        resultChecker.checkResult();

        //when, then
        ResultCheckerNotFoundException exception
                = assertThrows(ResultCheckerNotFoundException.class, () -> resultChecker.findPlayerByHash(hashIdToFind));
        assertEquals("Not found ticket with" + hashIdToFind, exception.getMessage());
    }
}