package pl.lotto.htttp.numbergenerator;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.winningnumbergenerator.RandomNumbersGenerable;
import pl.lotto.infrastructure.winningnumbergenerator.http.RandomGeneratorClientConfig;
import pl.lotto.infrastructure.winningnumbergenerator.http.RandomNumberGeneratorRestConfigurationProperties;
import pl.lotto.infrastructure.winningnumbergenerator.http.RestTemplateResponseErrorHandler;



@SpringBootTest
public class RandomNumberGeneratorRestTemplateIntegrationTestConfig extends RandomGeneratorClientConfig {

    public RandomNumberGeneratorRestTemplateIntegrationTestConfig(RandomNumberGeneratorRestConfigurationProperties properties) {
        super(properties);
    }

    public RandomNumbersGenerable remoteNumberGeneratorClient(){
        RestTemplateResponseErrorHandler restTemplateResponseErrorHandler = restTemplateResponseErrorHandler();
        RestTemplate restTemplate = restTemplate(restTemplateResponseErrorHandler);
        return remoteNumberGeneratorClient(restTemplate);
    }

}
