package pl.lotto.domain.resultchecker;

import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.util.List;

class ResultCheckerMapper {

    static List<Ticket> mapToTickets(List<TicketDto> ticketDtos) {
        return ticketDtos.stream()
                .map(ticketDto -> Ticket.builder()
                        .hash(ticketDto.hash())
                        .drawDate(ticketDto.drawDate())
                        .numbers(ticketDto.numbers())
                        .build())
                .toList();
    }

    public static List<ResultDto> mapToResults(List<Player> players) {
        return players.stream()
                .map(player -> ResultDto.builder()
                        .isWinner(player.isWinner())
                        .numbers(player.numbers())
                        .hitNumbers(player.hitNumbers())
                        .drawDate(player.drawDate())
                        .build())
                .toList();
    }
}
