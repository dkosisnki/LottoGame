package pl.lotto.infrastructure.numberreceiver.controller;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public record NumberReceiverRequestBodyDto(
        @NotNull(message = "{inputNumbers.not.null}")
        @NotEmpty(message = "{inputNumbers.not.empty}")
        List<Integer> inputNumbers) {
}
