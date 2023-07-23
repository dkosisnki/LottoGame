package pl.lotto.domain.resultannouncer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.resultchecker.ResultCheckerFacade;

import java.time.Clock;

@Configuration
public class ResultAnnouncerConfiguration {


    @Bean
    ResultAnnouncerFacade resultAnnouncerFacade(
            ResultCheckerFacade resultCheckerFacade,ResponseRepository responseRepository, Clock clock){
        return new ResultAnnouncerFacade(resultCheckerFacade,responseRepository,clock);
    }

}
