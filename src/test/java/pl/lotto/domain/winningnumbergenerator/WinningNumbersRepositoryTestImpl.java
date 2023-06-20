package pl.lotto.domain.winningnumbergenerator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class WinningNumbersRepositoryTestImpl implements WinningNumbersRepository{

    Map<LocalDateTime, WinningNumbers> repository = new ConcurrentHashMap<>();

    @Override
    public WinningNumbers save(WinningNumbers winningNumbers) {
        return repository.put(winningNumbers.drawDate(),winningNumbers);
    }

    @Override
    public Optional<WinningNumbers> findWonNumbersForDate(LocalDateTime date) {
        return Optional.ofNullable(repository.get(date));
    }
}
