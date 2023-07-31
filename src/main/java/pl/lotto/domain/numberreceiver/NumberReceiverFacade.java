package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.lotto.domain.numberreceiver.ValidationResult.INPUT_SUCCESS;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator numberValidator;
    private final DrawDateGenerator drawDateGenerator;
    private final HashGenerable hashGenerator;
    private final TicketRepository ticketRepository;


    public NumberReceiverResponseDto inputNumbers(Set<Integer> numbersFromUser) {
        List<ValidationResult> validationResultList = numberValidator.validate(numbersFromUser);
        if (!validationResultList.isEmpty()) {
            String resultMessage = numberValidator.createResultMessage();
            return new NumberReceiverResponseDto(null, resultMessage);
        }
        LocalDateTime drawDate = drawDateGenerator.getNextDrawDate();

        String hash = hashGenerator.getHash();

        TicketDto generatedTicket = TicketDto.builder()
                .ticketId(hash)
                .numbers(numbersFromUser)
                .drawDate(drawDate)
                .build();

        Ticket savedTicket = Ticket.builder()
                .ticketId(hash)
                .numbers(generatedTicket.numbers())
                .drawDate(generatedTicket.drawDate())
                .build();

        ticketRepository.save(savedTicket);

        return new NumberReceiverResponseDto(generatedTicket, INPUT_SUCCESS.info);
    }

    public List<TicketDto> retrieveAllTicketsByNextDrawDate() {
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        return retrieveAllTicketsByNextDrawDate(nextDrawDate);
    }

    public List<TicketDto> retrieveAllTicketsByNextDrawDate(LocalDateTime date) {
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        if (date.isAfter(nextDrawDate)) {
            return Collections.emptyList();
        }
        return ticketRepository.findAllByDrawDate(date)
                .stream()
                .map(ticket -> TicketDto.builder()
                        .ticketId(ticket.ticketId())
                        .numbers(ticket.numbers())
                        .drawDate(ticket.drawDate())
                        .build())
                .collect(Collectors.toList());
    }

    public LocalDateTime retrieveNextDrawDate() {
        return drawDateGenerator.getNextDrawDate();
    }
}
