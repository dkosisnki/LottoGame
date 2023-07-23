package pl.lotto.infrastructure.numberreceiver.controller;

import java.util.List;

public record NumberReceiverRequestBodyDto(List<Integer> inputNumbers) {
}
