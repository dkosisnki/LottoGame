package pl.lotto.domain.resultannouncer.dto;

import java.io.Serializable;

public record ResultAnnouncerResponseDto(
        ResultResponseDto resultResponseDto,
        String message
) implements Serializable {
}
