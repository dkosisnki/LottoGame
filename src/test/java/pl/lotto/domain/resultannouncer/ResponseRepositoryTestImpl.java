package pl.lotto.domain.resultannouncer;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseRepositoryTestImpl implements ResponseRepository{

    private Map<String, ResultResponse> repository = new ConcurrentHashMap<>();

    @Override
    public boolean hasHash(String hash) {
        return repository.containsKey(hash);
    }

    @Override
    public Optional<ResultResponse> findByHash(String hash) {
        return Optional.ofNullable(repository.get(hash));
    }

    @Override
    public ResultResponse saveResponse(ResultResponse resultResponse) {
        return repository.put(resultResponse.hash(),resultResponse);
    }
}
