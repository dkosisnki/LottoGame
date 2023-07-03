package pl.lotto.domain.resultannouncer;

import pl.lotto.domain.resultannouncer.dto.ResultResponseDto;

class ResultMapper {
    public static ResultResponseDto mapToResultResponseDto(ResultResponse resultResponse) {
        return ResultResponseDto.builder()
                .hash(resultResponse.hash())
                .numbers(resultResponse.numbers())
                .hitNumbers(resultResponse.hitNumbers())
                .drawDate(resultResponse.drawDate())
                .isWinner(resultResponse.isWinner())
                .build();
    }

    public static ResultResponse mapToResultResponse(ResultResponseDto resultResponseDto) {
        return ResultResponse.builder()
                .hash(resultResponseDto.hash())
                .numbers(resultResponseDto.numbers())
                .hitNumbers(resultResponseDto.hitNumbers())
                .drawDate(resultResponseDto.drawDate())
                .isWinner(resultResponseDto.isWinner())
                .build();
    }
}
