package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.PlayerDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;
import pl.lotto.domain.winningnumbergenerator.WinningNumbersGeneratorFacade;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ResultCheckerFacade {

    private final NumberReceiverFacade numberReceiverFacade;
    private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;
    private final WinnersRetriever winnersGenerator;
    private final PlayerRepository repository;

    public PlayerDto generateResults(){
        List<TicketDto> ticketsDto = numberReceiverFacade.retrieveAllTicketsByNextDrawDate();
        List<Ticket> tickets = ResultCheckerMapper.mapToTickets(ticketsDto);

        Set<Integer> winningNumbers = winningNumbersGeneratorFacade.generateWinningNumbers().winningNumbers();

        if (winningNumbers == null || winningNumbers.isEmpty()){
            return PlayerDto.builder()
                    .message("Winners failed to retrieve")
                    .build();
        }
        List<Player> players = winnersGenerator.retrieveWinners(winningNumbers,tickets);

        repository.saveAll(players);

        return PlayerDto.builder()
                .results(ResultCheckerMapper.mapToResults(players))
                .message("Winners success to retrieve")
                .build();
    }

    public ResultDto findByTicketId(String ticketId){
        Player player = repository.findByHash(ticketId)
                .orElseThrow(
                        () -> new ResultCheckerNotFoundException("Not found for id: " + ticketId));
        return ResultDto.builder()
                .hash(player.hash())
                .numbers(player.numbers())
                .hitNumbers(player.hitNumbers())
                .drawDate(player.drawDate())
                .isWinner(player.isWinner())
                .build();
    }

}
