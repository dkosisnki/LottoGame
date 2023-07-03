package pl.lotto.domain.winningnumbergenerator;

import pl.lotto.domain.winningnumbergenerator.dto.SixRandomNumberDto;

import java.util.Set;


public class RandomNumbersGeneratorTestImpl implements RandomNumbersGenerable {
    @Override
    public SixRandomNumberDto generateSixRandomNumbers() {
        return  SixRandomNumberDto.builder()
                .randomNumbers(Set.of(1,2,3,4,5,6))
                .build();
    }
}
