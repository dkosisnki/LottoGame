package pl.lotto.domain.numbeReceiver;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.numbeReceiver.dto.InputNumberResultDTO;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NumberReceiverFacadeTest {

    private final NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(
            new NumbersValidator()
    );

    @Test
    public void shouldReturnSuccessWhenUserGaveSixCorrectNumbers(){
        //given
        Set<Integer> usersNumbers = Set.of(1,2,3,4,5,6);
        //when
        InputNumberResultDTO result = numberReceiverFacade.inputNumbers(usersNumbers);
        //then
        assertEquals("Success",result.message());
    }

    @Test
    public void shouldReturnFailWhenUserGaveMoreThanSixNumbers(){
        //given
        Set<Integer> usersNumbers = Set.of(1,2,3,4,5,6,7);
        //when
        InputNumberResultDTO result = numberReceiverFacade.inputNumbers(usersNumbers);
        //then
        assertEquals("Fail",result.message());
    }

    @Test
    public void shouldReturnFailWhenUserGaveLessThanSixNumbers(){
        //given
        Set<Integer> usersNumbers = Set.of(1,2,3,4,5);
        //when
        InputNumberResultDTO result = numberReceiverFacade.inputNumbers(usersNumbers);
        //then
        assertEquals("Fail",result.message());
    }

    @Test
    public void shouldReturnFailWhenUserGaveNumberOutOfRange(){
        //given
        Set<Integer> usersNumbers = Set.of(1,2,3,4,5,3000);
        //when
        InputNumberResultDTO result = numberReceiverFacade.inputNumbers(usersNumbers);
        //then
        assertEquals("Fail",result.message());
    }

}