package pl.lotto.infrastructure.resultannouncer.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.lotto.domain.resultchecker.ResultCheckerNotFoundException;


@ControllerAdvice
@Slf4j
public class ResultAnnouncerControllerExceptionHandler {

    @ExceptionHandler(ResultCheckerNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResultAnnouncerExceptionResponse handleResultAnnouncerNotFoundException(ResultCheckerNotFoundException e){
        String message = e.getMessage();
        log.error(message);
        return new ResultAnnouncerExceptionResponse(message, HttpStatus.NOT_FOUND);
    }

}
