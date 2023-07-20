package pl.lotto.infrastructure.winningnumbergenerator.http;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.lotto.domain.winningnumbergenerator.RandomNumbersGenerable;
import pl.lotto.domain.winningnumbergenerator.dto.SixRandomNumbersDto;

import java.util.*;
import java.util.stream.Collectors;


@AllArgsConstructor
@Slf4j
public class RandomNumberGeneratorRestTemplate implements RandomNumbersGenerable {


    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;


    @Override
    public SixRandomNumbersDto generateSixRandomNumbers(int count, int lowerBand, int upperBand) {
        log.info("Started fetching winning numbers using http client");
        String urlForService = getUrlForService("/api/v1.0/random");
        HttpHeaders headers = new HttpHeaders();
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<Integer>> response = makeGetRequest(count, lowerBand, upperBand, urlForService, requestEntity);
            Set<Integer> sixDistinctNumber = getSixRandomDistinctNumbers(response);
            if (sixDistinctNumber.size() != 6){
                log.error("Set is less than: {} Have to request one more time!", count);
                generateSixRandomNumbers(count, lowerBand, upperBand);
            }
            return SixRandomNumbersDto.builder()
                    .numbers(sixDistinctNumber)
                    .build();
        } catch (ResourceAccessException e){
            log.error("Error while fetching winning numbers using http clients" + e.getMessage());
            return SixRandomNumbersDto.builder().build();
        }
    }

    private Set<Integer> getSixRandomDistinctNumbers(ResponseEntity<List<Integer>> response) {
        List<Integer> numbers = response.getBody();
        if (Objects.isNull(numbers)){
            log.error("Response Body was null returning empty collection");
            return Collections.emptySet();
        }
        HashSet<Integer> distinctNumbers = new HashSet<>(numbers);
        return distinctNumbers.stream()
                .limit(6)
                .collect(Collectors.toSet());
    }

    private ResponseEntity<List<Integer>> makeGetRequest(int count, int lowerBand, int upperBand, String urlForService, HttpEntity<HttpHeaders> requestEntity) {
        final String url = UriComponentsBuilder.fromHttpUrl(urlForService)
                .queryParam("min", lowerBand)
                .queryParam("max", upperBand)
                .queryParam("count", count)
                .toUriString();
        ResponseEntity<List<Integer>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });
        return response;
    }

    private String getUrlForService(String service) {
        return uri + ":" + port + service;
    }
}
