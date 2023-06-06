package pl.lotto.domain.numbeReceiver;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numbeReceiver.dto.InputNumberResultDTO;

import java.util.Set;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumbersValidator validator;

    public InputNumberResultDTO inputNumbers(Set<Integer> numbers) {
        boolean areNumbersCorrect = validator.areNumbersCorrect(numbers);
        if (areNumbersCorrect){
            return InputNumberResultDTO.builder()
                    .message("Success")
                    .build();
        } else return InputNumberResultDTO.builder()
                .message("Fail")
                .build();
    }


}
