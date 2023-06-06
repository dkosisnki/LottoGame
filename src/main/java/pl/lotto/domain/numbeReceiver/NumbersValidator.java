package pl.lotto.domain.numbeReceiver;

import java.util.Set;

class NumbersValidator {

    private final static Integer MIN_RANGE = 1;
    private final static Integer MAX_RANGE = 99;
    private final static Integer CORRECT_AMOUNT = 6;

    boolean areNumbersCorrect(Set<Integer> numbers) {
        return numbers.stream()
                .filter(number -> number >= MIN_RANGE)
                .filter(number -> number <= MAX_RANGE)
                .count() == CORRECT_AMOUNT;
    }

}
