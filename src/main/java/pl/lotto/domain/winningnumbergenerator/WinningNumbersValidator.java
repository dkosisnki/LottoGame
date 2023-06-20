package pl.lotto.domain.winningnumbergenerator;

import java.util.Set;

class WinningNumbersValidator {
    private static final int MAX_VALUE = 99;
    private static final int MIN_VALUE = 1;

    public void validate(Set<Integer> winningNumbers) {
        if (isSomeNumberOutOfRange(winningNumbers)){
            throw new IllegalStateException("Number out of range!");
        }
    }

    private boolean isSomeNumberOutOfRange(Set<Integer> winningNumbers) {
        return winningNumbers.stream()
                .anyMatch((number) -> number < MIN_VALUE || number > MAX_VALUE);
    }


}
