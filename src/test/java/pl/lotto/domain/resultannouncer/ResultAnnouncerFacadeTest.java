package pl.lotto.domain.resultannouncer;


import org.junit.jupiter.api.Test;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultchecker.ResultCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.lotto.domain.resultannouncer.MessageResponse.*;

class ResultAnnouncerFacadeTest {

    ResultCheckerFacade resultCheckerFacade = mock(ResultCheckerFacade.class);
    ResponseRepository responseRepository = new ResponseRepositoryTestImpl();
    Clock clock = Clock.systemUTC();


    @Test
    public void shouldReturnCorrectMessageWhenThereIsNoPlayerWithAppropriateHash() {
        //given
        String hashWhichWeLookFor = "someHash";
        when(resultCheckerFacade.findByHash(hashWhichWeLookFor)).thenReturn(null);
        ResultAnnouncerFacade resultAnnouncerFacade =
                new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade,responseRepository,clock);
        //when
        ResultAnnouncerResponseDto resultDto = resultAnnouncerFacade.checkWinner(hashWhichWeLookFor);
        //then
        assertEquals(HASH_DOES_NOT_EXIST_MESSAGE.message, resultDto.message());
    }

    @Test
    public void shouldReturnCorrectMessageWhenResultIsAlreadyCached() {
        //given
        String hashWhichWeLookFor = "someHash";
        when(resultCheckerFacade.findByHash(hashWhichWeLookFor)).thenReturn(ResultDto.builder()
                .hash(hashWhichWeLookFor)
                        .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                        .numbers(Set.of(1,2,3,4,5,6))
                        .hitNumbers(Set.of(1,2,3,13,17,56))
                        .isWinner(true)
                .build()
        );
        ResultAnnouncerFacade resultAnnouncerFacade =
                new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade,responseRepository,clock);
        resultAnnouncerFacade.checkWinner(hashWhichWeLookFor);
        //when
        ResultAnnouncerResponseDto resultDto = resultAnnouncerFacade.checkWinner(hashWhichWeLookFor);
        //then
        assertEquals(ALREADY_CHECKED_MESSAGE.message, resultDto.message());
    }

    @Test
    public void shouldReturnCorrectMessageWhenUserTryToCheckResultBeforeResultAnnouncement() {
        //given
        Clock clock = Clock.fixed(
                LocalDateTime.of(2023,6,24,11,0,0).toInstant(ZoneOffset.UTC),
                ZoneId.of("UTC"));
        String hashWhichWeLookFor = "someHash";
        when(resultCheckerFacade.findByHash(hashWhichWeLookFor)).thenReturn(ResultDto.builder()
                .hash(hashWhichWeLookFor)
                .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                .numbers(Set.of(1,2,3,4,5,6))
                .hitNumbers(Set.of(1,2,3,13,17,56))
                .isWinner(true)
                .build()
        );
        ResultAnnouncerFacade resultAnnouncerFacade =
                new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade,responseRepository,clock);
        //when
        ResultAnnouncerResponseDto resultDto = resultAnnouncerFacade.checkWinner(hashWhichWeLookFor);
        //then
        assertEquals(WAIT_MESSAGE.message, resultDto.message());
    }

    @Test
    public void shouldReturnCorrectMessageWhenUserWon() {
        //given
        String hashWhichWeLookFor = "someHash";
        when(resultCheckerFacade.findByHash(hashWhichWeLookFor)).thenReturn(ResultDto.builder()
                .hash(hashWhichWeLookFor)
                .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                .numbers(Set.of(1,2,3,4,5,6))
                .hitNumbers(Set.of(2,4,5,9,16,56))
                .isWinner(true)
                .build()
        );
        ResultAnnouncerFacade resultAnnouncerFacade =
                new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade,responseRepository,clock);
        //when
        ResultAnnouncerResponseDto resultDto = resultAnnouncerFacade.checkWinner(hashWhichWeLookFor);
        //then
        assertEquals(WIN_MESSAGE.message, resultDto.message());
    }

    @Test
    public void shouldReturnCorrectMessageWhenUserLose() {
        //given
        String hashWhichWeLookFor = "someHash";
        when(resultCheckerFacade.findByHash(hashWhichWeLookFor)).thenReturn(ResultDto.builder()
                .hash(hashWhichWeLookFor)
                .drawDate(LocalDateTime.of(2023,6,24,12,0,0))
                .numbers(Set.of(1,2,3,4,5,6))
                .hitNumbers(Set.of(2,8,16,19,26,56))
                .isWinner(false)
                .build()
        );
        ResultAnnouncerFacade resultAnnouncerFacade =
                new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade,responseRepository,clock);
        //when
        ResultAnnouncerResponseDto resultDto = resultAnnouncerFacade.checkWinner(hashWhichWeLookFor);
        //then
        assertEquals(LOSE_MESSAGE.message, resultDto.message());
    }
}