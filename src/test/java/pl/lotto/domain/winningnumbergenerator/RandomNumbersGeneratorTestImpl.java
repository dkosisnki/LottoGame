package pl.lotto.domain.winningnumbergenerator;

import pl.lotto.domain.winningnumbergenerator.dto.SixRandomNumbersDto;

import java.util.Set;


public class RandomNumbersGeneratorTestImpl implements RandomNumbersGenerable {


    @Override
    public SixRandomNumbersDto generateSixRandomNumbers(int count, int lowerBand, int upperBand) {
        return  SixRandomNumbersDto.builder()
                .numbers(Set.of(1,2,3,4,5,6))
                .build();
    }
}
