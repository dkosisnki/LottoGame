package pl.lotto.domain.resultannouncer;

import java.util.Optional;

public interface ResponseRepository {
    boolean hasHash(String hash);

    Optional<ResultResponse> findByHash(String hash);

    ResultResponse saveResponse(ResultResponse resultResponse);
}
