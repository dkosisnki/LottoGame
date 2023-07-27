package pl.lotto.infrastructure.resultchecker.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lotto.domain.resultchecker.ResultCheckerFacade;
import pl.lotto.domain.resultchecker.dto.PlayerDto;
import pl.lotto.domain.winningnumbergenerator.WinningNumbersGeneratorFacade;

@Component
@Slf4j
@AllArgsConstructor
public class ResultCheckerScheduler {

    private final ResultCheckerFacade resultCheckerFacade;
    private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;

    @Scheduled(cron = "${lotto.number-generator.lotteryResultCheckerOccurrence}")
    void generateWinners(){
        log.info("ResultCheckerScheduler started");
        if (!winningNumbersGeneratorFacade.areWinningsNumberGeneratedByDate()){
            log.error("Winning numbers are not generated.");
            throw new RuntimeException("Winning numbers are not generated");
        }
        PlayerDto result = resultCheckerFacade.generateResults();
        log.info(result.toString());
    }

}
