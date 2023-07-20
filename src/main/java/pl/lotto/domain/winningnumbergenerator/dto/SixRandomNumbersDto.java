package pl.lotto.domain.winningnumbergenerator.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record SixRandomNumbersDto(Set<Integer> numbers) {
}
