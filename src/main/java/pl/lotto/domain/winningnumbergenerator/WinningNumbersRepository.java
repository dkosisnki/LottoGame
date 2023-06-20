package pl.lotto.domain.winningnumbergenerator;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WinningNumbersRepository {
    WinningNumbers save(WinningNumbers winningNumbers);

    Optional<WinningNumbers> findWonNumbersForDate(LocalDateTime date);
}
