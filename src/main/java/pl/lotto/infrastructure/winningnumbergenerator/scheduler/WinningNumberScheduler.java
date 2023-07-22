package pl.lotto.infrastructure.winningnumbergenerator.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lotto.domain.winningnumbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.winningnumbergenerator.dto.WinningNumbersDto;


@Component
@AllArgsConstructor
@Slf4j
public class WinningNumberScheduler {

    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;

    @Scheduled(cron = "${lotto.number-generator.lotteryRunOccurrence}")
    void generateNumbers(){
        log.info("winning numbers scheduler started");
        WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.generateWinningNumbers();
        log.info(winningNumbersDto.winningNumbers().toString());
        log.info(winningNumbersDto.drawDate().toString());
    }

}
