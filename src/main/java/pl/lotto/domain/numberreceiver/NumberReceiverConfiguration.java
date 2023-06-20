package pl.lotto.domain.numberreceiver;

import java.time.Clock;

public class NumberReceiverConfiguration {
    public NumberReceiverFacade createForTest(Clock clock, TicketRepository ticketRepository) {
        NumberValidator numberValidator = new NumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        HashGenerable hashGenerator = new HashGenerator();
        return new NumberReceiverFacade(numberValidator, drawDateGenerator, hashGenerator, ticketRepository);
    }
}
