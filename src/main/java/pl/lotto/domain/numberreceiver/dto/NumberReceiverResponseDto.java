package pl.lotto.domain.numberreceiver.dto;

public record NumberReceiverResponseDto(
        TicketDto ticketDto,
        String message
) {
}
