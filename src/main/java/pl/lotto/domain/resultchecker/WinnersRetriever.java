package pl.lotto.domain.resultchecker;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WinnersRetriever {

    private static final Integer MIN_AMOUNT_OF_NUMBERS_TO_WIN = 3;
    public List<Player> retrieveWinners(Set<Integer> winningNumbers, List<Ticket> tickets) {
       return tickets.stream()
                .map(ticket -> {
                        Set<Integer> hitNumbers = calculateHits(winningNumbers, ticket);
                        return buildPlayer(ticket, hitNumbers);
                })
                .toList();
    }

    private Set<Integer> calculateHits(Set<Integer> winningNumbers, Ticket ticket) {
        return ticket.numbers().stream()
                .filter(winningNumbers::contains)
                .collect(Collectors.toSet());
    }

    private Player buildPlayer(Ticket ticket, Set<Integer> hitNumbers) {
        Player.PlayerBuilder player = Player.builder();
        if (isWinner(hitNumbers)){
            player.isWinner(true);
        }
        return player.hash(ticket.hash())
                .drawDate(ticket.drawDate())
                .hitNumbers(hitNumbers)
                .numbers(ticket.numbers())
                .build();
    }

    private boolean isWinner(Set<Integer> hitNumbers) {
        return hitNumbers.size() > MIN_AMOUNT_OF_NUMBERS_TO_WIN;
    }
}
