package pl.lotto.infrastructure.resultannouncer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.resultannouncer.ResultAnnouncerFacade;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;

@RestController
@AllArgsConstructor
public class ResultAnnouncerController {

    private final ResultAnnouncerFacade resultAnnouncerFacade;

    @GetMapping("/results/{id}")
    public ResponseEntity<ResultAnnouncerResponseDto> checkWinner(@PathVariable String id){
        ResultAnnouncerResponseDto response = resultAnnouncerFacade.checkWinner(id);
        return ResponseEntity.ok(response);
    }
}
