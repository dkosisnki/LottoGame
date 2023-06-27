package pl.lotto.domain.resultchecker;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerRepositoryTestImpl implements PlayerRepository{

    Map<String, Player> repository = new ConcurrentHashMap<>();

    @Override
    public List<Player> saveAll(List<Player> players) {
        players.forEach(player -> repository.put(player.hash(),player));
        return players;
    }

    @Override
    public Optional<Player> findByHash(String hashId) {
        return Optional.ofNullable(repository.get(hashId));
    }
}
