package pl.lotto.domain.resultannouncer.dto;

public record ResultAnnouncerResponseDto(
        ResultResponseDto resultResponseDto,
        String message) {
}
