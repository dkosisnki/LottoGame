package pl.lotto.domain.resultannouncer;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultannouncer.dto.ResultResponseDto;
import pl.lotto.domain.resultchecker.ResultCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static pl.lotto.domain.resultannouncer.MessageResponse.*;

@AllArgsConstructor
public class ResultAnnouncerFacade {

    public static final LocalTime RESULTS_ANNOUNCEMENT_TIME = LocalTime.of(12, 0);
    private final ResultCheckerFacade resultCheckerFacade;
    private final ResponseRepository responseRepository;
    private final Clock clock;

    @Cacheable("checkWinner")
    public ResultAnnouncerResponseDto checkWinner(String ticketId){
        ResultDto resultDto = resultCheckerFacade.findByTicketId(ticketId);
        if (resultDto == null) {
            return new ResultAnnouncerResponseDto(null, HASH_DOES_NOT_EXIST_MESSAGE.message);
        }

        ResultResponseDto resultResponseDto = buildResponseDto(resultDto);
        responseRepository.save(ResultMapper.mapToResultResponse(resultResponseDto));

        if (responseRepository.existsById(ticketId) && !isAfterResultAnnouncementTime(resultDto)){
            return new ResultAnnouncerResponseDto(resultResponseDto, WAIT_MESSAGE.message);
        }
        if (resultCheckerFacade.findByTicketId(ticketId).isWinner()) {
            return new ResultAnnouncerResponseDto(resultResponseDto, WIN_MESSAGE.message);
        }
        return new ResultAnnouncerResponseDto(resultResponseDto, LOSE_MESSAGE.message);
    }

    private boolean isAfterResultAnnouncementTime(ResultDto resultDto) {
        LocalDateTime announcementDateTime =
                LocalDateTime.of(resultDto.drawDate().toLocalDate(), RESULTS_ANNOUNCEMENT_TIME);
        return LocalDateTime.now(clock).isAfter(announcementDateTime);
    }

    private static ResultResponseDto buildResponseDto(ResultDto resultDto) {
        return ResultResponseDto.builder()
                .ticketId(resultDto.hash())
                .numbers(resultDto.numbers())
                .hitNumbers(resultDto.hitNumbers())
                .drawDate(resultDto.drawDate())
                .isWinner(resultDto.isWinner())
                .build();
    }
}
