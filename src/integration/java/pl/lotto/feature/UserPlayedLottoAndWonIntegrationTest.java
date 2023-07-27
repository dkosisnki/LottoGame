package pl.lotto.feature;


import com.github.tomakehurst.wiremock.client.WireMock;
import org.awaitility.Durations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.BaseIntegrationTest;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultchecker.ResultCheckerFacade;
import pl.lotto.domain.resultchecker.ResultCheckerNotFoundException;
import pl.lotto.domain.winningnumbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.winningnumbergenerator.WinningNumbersNotFoundException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserPlayedLottoAndWonIntegrationTest extends BaseIntegrationTest {

    @Autowired
    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;

    @Autowired
    ResultCheckerFacade resultCheckerFacade;

    @Test
    public void shouldUserWinAndSystemShouldGenerateWinners() throws Exception {

        //step 1: external service returns 6 random numbers(1,2,3,4,5,6)
//        given
        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=25")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-type", "application/json")
                        .withBody("""
                                [1, 2, 3, 4, 5, 6, 82, 82, 83, 83, 86, 57, 10, 81, 53, 93, 50, 54, 31, 88, 15, 43, 79, 32, 43]
                                """)
                ));
        //step 2: system fetched winning numbers for draw date: 08.07.2023 12:00
        //given
        LocalDateTime drawDate = LocalDateTime.of(2023, 7, 8, 12, 0, 0);
        //when & then
        await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Durations.ONE_SECOND)
                .until(() -> {
                    try {
                        return !winningNumbersGeneratorFacade.retrieveWonNumbersForDate(drawDate).winningNumbers().isEmpty();
                    } catch (WinningNumbersNotFoundException e) {
                        return false;
                    }
                });
        //step 3: user made POST /inputNumbers with 6 numbers (1, 2, 3, 4, 5, 6) at 05-07-2023 11:00 and system returned OK(200) with message: “success” and Ticket (DrawDate:8.7.2023 12:00 (Saturday), TicketId: sampleTicketId)
        //given
        ResultActions performPostWithInputNumbers = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers": [1,2,3,4,5,6]
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON));
        //when
        MvcResult mvcResult = performPostWithInputNumbers.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        NumberReceiverResponseDto response = objectMapper.readValue(json, NumberReceiverResponseDto.class);
        String ticketId = response.ticketDto().hash();
        //then
        assertAll(
                () -> assertEquals(response.ticketDto().drawDate(), drawDate),
                () -> assertEquals(response.message(), "SUCCESS"),
                () -> assertTrue(Objects.nonNull(response.ticketDto().hash()))
        );
        //step 4: user made GET /results/notExistingId and system returned 404(NOT_FOUND) and body with (message: Not found for id: notExistingId and status NOT_FOUND)
        //given & when
        ResultActions performGetForNonExistingId = mockMvc.perform(get("/results/nonExistingId"));
        //then
        performGetForNonExistingId.andExpect(status().isNotFound()).andExpect(content().json("""
                  {
                   "message": "Not found for id: nonExistingId",
                   "status": "NOT_FOUND"
                  }
                """.trim()
        ));


        //step 5: 3 days and 55 minutes passed, and it is 5 minute before draw (08.07.2023 11:55)
        //given & when & then
        clock.plusDaysAndMinutes(3, 55);


        //step 6: system generated result for TicketId with draw date 19.11.2022 12:00, and saved it with 6 hits
        await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Durations.ONE_SECOND)
                .until(() -> {
                    try {
                        return !resultCheckerFacade.findByTicketId(ticketId).numbers().isEmpty();
                    } catch (ResultCheckerNotFoundException e) {
                        return false;
                    }
                });


        //step 7: 6 minutes passed, and it is 1 minute after the draw (08.07.2023 12:01)
        //given & when & then
        clock.plusMinutes(6);


        //step 8: user made GET /result/sampleTicketId and system returned 200(OK)
        //given
        ResultActions performGetForExistingId = mockMvc.perform(get("/results/" + ticketId));
        //when & then
        MvcResult result = performGetForExistingId.andExpect(status().isOk()).andReturn();
        String jsonResult = result.getResponse().getContentAsString();
        ResultAnnouncerResponseDto responseFromAnnouncer = objectMapper.readValue(jsonResult, ResultAnnouncerResponseDto.class);
        assertAll(
                () -> assertEquals("Congratulations, you won!", responseFromAnnouncer.message()),
                () -> assertEquals(ticketId, responseFromAnnouncer.resultResponseDto().ticketId())
        );
    }

}
