package pl.lotto.domain.winningnumbergenerator;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
@Builder
record WinningNumbers(
        Set<Integer> numbers,
        LocalDateTime drawDate
        ) {
}
