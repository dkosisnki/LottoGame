package pl.lotto.domain.winningnumbergenerator;

import java.util.Set;

public class RandomNumbersGeneratorTestImpl implements RandomNumbersGenerable {
    @Override
    public Set<Integer> generateWinningNumbers() {
        return Set.of(1,2,3,4,5,100);
    }
}
