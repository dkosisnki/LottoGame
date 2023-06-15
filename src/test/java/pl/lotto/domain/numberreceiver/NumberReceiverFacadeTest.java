package pl.lotto.domain.numberreceiver;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.AdjustableClock;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import static org.junit.jupiter.api.Assertions.*;


public class NumberReceiverFacadeTest {

    private final TicketRepository ticketRepository = new TicketRepositoryTestImpl();
    private final HashGenerable hashGenerator = new HashGenerator();
    Clock clock = Clock.systemUTC();

    @Test
    public void shouldReturnSuccessMessageWhenUserGaveCorrectNumbers() {
        // given
        Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5,6);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(userNumbers);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(response.ticketDto(), ValidationResult.INPUT_SUCCESS.info);
        assertEquals(expectedResponse,response);
    }

    @Test
    public void shouldReturnFailedMessageWhenUserGaveNumberOutOfRange() {
        // given
        Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5,300);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(userNumbers);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertEquals(expectedResponse,response);
    }

    @Test
    public void shouldReturnFailedMessageWhenUserGaveLessThanSixNumbers() {
        // given
        Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(userNumbers);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
        assertEquals(expectedResponse,response);
    }

    @Test
    public void shouldReturnFailedMessageWhenUserGaveMoreThanSixNumbers() {
        // given
        Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5, 6, 7);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(userNumbers);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
        assertEquals(expectedResponse,response);
    }

    @Test
    public void shouldReturnCorrectHash() {
        // given
        Set<Integer> usersNumbers = Set.of(1, 2, 3, 4, 5, 6);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);

        // when
        String testedHash = numberReceiverFacade.inputNumbers(usersNumbers).ticketDto().hash();

        // then
        assertEquals(36,testedHash.length());
        assertNotNull(testedHash);
    }

    @Test
    public void shouldReturnCorrectDrawDate() {
        // given
        Set<Integer> usersNumbers = Set.of(1, 2, 3, 4, 5, 6);

        Clock clock = Clock.fixed(
                LocalDateTime.of(2023, 6, 14, 10, 0, 0)
                        .toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));

        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(usersNumbers).ticketDto().drawDate();

        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2023, 6, 17, 12, 0, 0);
        assertEquals(expectedDrawDate,testedDrawDate);
    }

    @Test
    public void shouldReturnNextSaturdayDrawDateWhenIsSaturdayNoon() {
        // given
//        Clock clock = Clock.fixed(LocalDateTime.of(2022, 11, 19, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        Set<Integer> userNumbers = Set.of(1, 2, 3, 4, 5, 6);

        Clock clock = Clock.fixed(
                LocalDateTime.of(2023, 6, 17, 12, 0, 0)
                        .toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));

        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(userNumbers).ticketDto().drawDate();

        // then

        LocalDateTime expectedDrawDate = LocalDateTime.of(2023, 6, 24, 12, 0, 0);
        assertEquals(expectedDrawDate,testedDrawDate);
    }

    @Test
    public void shouldReturnNextSaturdayDrawDateWhenIsSaturdayAfternoon() {
        // given
//        Clock clock = Clock.fixed(LocalDateTime.of(2022, 11, 19, 14, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        Set<Integer> usersNumbers = Set.of(1, 2, 3, 4, 5, 6);

        Clock clock = Clock.fixed(
                LocalDateTime.of(2023, 6, 17, 14, 0, 0)
                        .toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));

        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(usersNumbers).ticketDto().drawDate();
        // then

        LocalDateTime expectedDrawDate = LocalDateTime.of(2023, 6, 24, 12, 0, 0);
        assertEquals(expectedDrawDate,testedDrawDate);

    }

    @Test
    public void shouldReturnTicketsWithCorrectDrawDate() {
        // given
        AdjustableClock clock = new AdjustableClock(
                LocalDateTime.of(2023, 6, 15, 12, 0, 0)
                        .toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        Set<Integer> usersNumbers = Set.of(1, 2, 3, 4, 5, 6);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);

        NumberReceiverResponseDto numberReceiverResponseDto1 = numberReceiverFacade.inputNumbers(usersNumbers);
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto2 = numberReceiverFacade.inputNumbers(usersNumbers);
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto3 = numberReceiverFacade.inputNumbers(usersNumbers);

        TicketDto ticketDto1 = numberReceiverResponseDto1.ticketDto();
        TicketDto ticketDto2 = numberReceiverResponseDto2.ticketDto();
        TicketDto ticketDto3 = numberReceiverResponseDto3.ticketDto();

        LocalDateTime drawDate = numberReceiverResponseDto2.ticketDto().drawDate();

        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);

        // then
        assertEquals(2, allTicketsByDate.size());
        assertTrue(allTicketsByDate.contains(ticketDto1));
        assertTrue(allTicketsByDate.contains(ticketDto2));
        assertFalse(allTicketsByDate.contains(ticketDto3));
    }
    @Test
    public void shouldReturnEmptyListIfThereAreNoTickets() {
        // given
        Clock clock = Clock.fixed(
                LocalDateTime.of(2023, 6, 15, 12, 0, 0)
                        .toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));

        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);

        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate();

        // then
        assertTrue(allTicketsByDate.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListIfGivenDateIsAfterNextDrawDate() {
        // given
        Set<Integer> usersNumbers = Set.of(1, 2, 3, 4, 5, 6);

        Clock clock = Clock.fixed(
                LocalDateTime.of(2023, 6, 15, 12, 0, 0)
                        .toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));

        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(usersNumbers);
        LocalDateTime drawDate = numberReceiverFacade.retrieveNextDrawDate();

        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate.plusWeeks(1));
        // then
        assertTrue(allTicketsByDate.isEmpty());
    }
}
