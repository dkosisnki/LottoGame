package pl.lotto.infrastructure.apicationvalidation;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ApiValidationResponseDto(
        List<String> message,
        HttpStatus status
) {
}
