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
import pl.lotto.domain.winningnumbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.winningnumbergenerator.WinningNumbersNotFoundException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserPlayedLottoAndWonIntegrationTest extends BaseIntegrationTest {

    @Autowired
    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;

    @Test
    public void shouldUserWinAndSystemShouldGenerateWinners() throws Exception {

        //step 1: external service returns 6 random numbers(1,2,3,4,5,6)
//        given
        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=25")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-type", "application/json")
                        .withBody("""
                                [1,2,3,4,5,6,12,19,24,29,32,39,42,48,51,53,55,56,67,74,81,89,90,94,98]
                                """)
                ));
        //step 2: system fetched winning numbers for draw date: 08.07.2023 12:00
        //given
        LocalDateTime drawDate = LocalDateTime.of(2023, 7, 8, 12, 0, 0);
        //when
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
        //step 3: user made POST /inputNumbers with 6 numbers (1, 2, 3, 4, 5, 6) at 16-11-2022 10:00 and system returned OK(200) with message: “success” and Ticket (DrawDate:19.11.2022 12:00 (Saturday), TicketId: sampleTicketId)
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
        //then
        assertAll(
                () -> assertEquals(response.ticketDto().drawDate(), drawDate),
                () -> assertEquals(response.message(), "SUCCESS"),
                () -> assertTrue(Objects.nonNull(response.ticketDto().hash()))
        );
        //step 4: user made GET /results/notExistingId and system returned 404(NOT_FOUND) and body with (message: Not found for id: notExistingId and status NOT_FOUND)
        //given
        ResultActions performGetForNonExistingId = mockMvc.perform(get("/results/nonExistingId"));
        performGetForNonExistingId.andExpect(status().isNotFound()).andExpect(content().json("""
                          {
                           "message": "Not found for id: nonExistingId",
                           "status": "NOT_FOUND"
                          }
                        """.trim()
        ));
        //when
        //then
        //step 5: 3 days and 55 minutes passed, and it is 5 minute before draw (19.11.2022 11:55)

    }

}
