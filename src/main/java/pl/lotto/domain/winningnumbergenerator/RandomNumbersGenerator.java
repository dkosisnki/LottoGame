package pl.lotto.domain.winningnumbergenerator;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class RandomNumbersGenerator implements RandomNumbersGenerable {

    @Override
    public Set<Integer> generateWinningNumbers() {
        Random random = new Random();
        return IntStream.generate(() -> random.nextInt(99)+1)
                .boxed()
                .distinct()
                .limit(6)
                .collect(Collectors.toSet());
    }
}
