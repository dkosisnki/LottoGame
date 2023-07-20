package pl.lotto.domain.winningnumbergenerator;

import pl.lotto.domain.winningnumbergenerator.dto.SixRandomNumbersDto;


public interface RandomNumbersGenerable {

    SixRandomNumbersDto generateSixRandomNumbers(int count,int lowerBand, int upperBand);
}
