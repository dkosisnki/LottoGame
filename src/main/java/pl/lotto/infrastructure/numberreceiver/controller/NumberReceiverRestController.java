package pl.lotto.infrastructure.numberreceiver.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@AllArgsConstructor
public class NumberReceiverRestController {

    private final NumberReceiverFacade numberReceiverFacade;

    @PostMapping("/inputNumbers")
    public ResponseEntity<NumberReceiverResponseDto> inputNumbers(@RequestBody @Valid NumberReceiverRequestBodyDto requestBody){
        Set<Integer> userNumbers = new HashSet<>(requestBody.inputNumbers());
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(userNumbers);
        return ResponseEntity.ok(response);
    }
}
