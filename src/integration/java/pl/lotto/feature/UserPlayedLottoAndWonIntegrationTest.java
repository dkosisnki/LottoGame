package pl.lotto.feature;


import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.lotto.BaseIntegrationTest;
import pl.lotto.domain.winningnumbergenerator.RandomNumbersGenerable;


public class UserPlayedLottoAndWonIntegrationTest extends BaseIntegrationTest {

    @Autowired
    RandomNumbersGenerable randomNumbersGenerable;

    @Test
    public void shouldUserWinAndSystemShouldGenerateWinners() {

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
        //when
        //step 2: system fetched winning numbers for draw date: 19.11.2022 12:00
        //step 3: user made POST /inputNumbers with 6 numbers (1, 2, 3, 4, 5, 6) at 16-11-2022 10:00 and system returned OK(200) with message: “success” and Ticket (DrawDate:19.11.2022 12:00 (Saturday), TicketId: sampleTicketId)
        //step 4: user made GET /results/notExistingId and system returned 404(NOT_FOUND) and body with (message: Not found for id: notExistingId and status NOT_FOUND)
        //step 5: 3 days and 55 minutes passed, and it is 5 minute before draw (19.11.2022 11:55)

    }

}
