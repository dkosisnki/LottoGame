package pl.lotto.infrastructure.resultannouncer.controller.error;

import org.springframework.http.HttpStatus;

public record ResultAnnouncerExceptionResponse(
        String message,
        HttpStatus status
) {
}
