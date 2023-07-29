package pl.lotto.htttp.numbergenerator;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.lotto.domain.winningnumbergenerator.RandomNumbersGenerable;
import pl.lotto.infrastructure.winningnumbergenerator.http.RandomNumberGeneratorRestConfigurationProperties;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.lotto.BaseIntegrationTest.WIRE_MOCK_HOST;


public class RandomNumberGeneratorRestTemplateTest {

    private final static String CONTENT_TYPE_HEADER_KEY = "Content-Type";
    private final static String APPLICATION_JSON_CONTENT_TYPE_VALUE = "application/json";

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    private final RandomNumberGeneratorRestConfigurationProperties properties =
            new RandomNumberGeneratorRestConfigurationProperties(
                    WIRE_MOCK_HOST, wireMockServer.getPort(), 1000, 1000
            );
    RandomNumbersGenerable randomNumbersGenerable =
            new RandomNumberGeneratorRestTemplateIntegrationTestConfig(properties).remoteNumberGeneratorClient();


    @Test
    void shouldThrowException500WhenFaultConnectionResetByPeer() {
        //given
        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=25")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON_CONTENT_TYPE_VALUE)
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));
        //when && then
        ResponseStatusException response = assertThrows(
                ResponseStatusException.class,
                () -> randomNumbersGenerable.generateSixRandomNumbers(25, 1, 99)
        );
        assertEquals("500 INTERNAL_SERVER_ERROR", response.getMessage());
    }

    @Test
    void shouldThrowException500WhenFaultEmptyResponse() {
        //given
        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=25")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON_CONTENT_TYPE_VALUE)
                        .withFault(Fault.EMPTY_RESPONSE)));
        //when && then
        ResponseStatusException response = assertThrows(
                ResponseStatusException.class,
                () -> randomNumbersGenerable.generateSixRandomNumbers(25, 1, 99)
        );
        assertEquals("500 INTERNAL_SERVER_ERROR", response.getMessage());
    }

    @Test
    void shouldThrowException204WhenServerResponseHadNoContentStatus() {
        //given
        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=25")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NO_CONTENT.value())
                        .withHeader(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON_CONTENT_TYPE_VALUE)
                        .withBody("""
                                [1, 2, 3, 4, 5, 6, 82, 82, 83, 83, 86, 57, 10, 81, 53, 93, 50, 54, 31, 88, 15, 43, 79, 32, 43]
                                      """.trim()
                        )));
        //when && then
        ResponseStatusException response = assertThrows(
                ResponseStatusException.class,
                () -> randomNumbersGenerable.generateSixRandomNumbers(25, 1, 99)
        );
        assertEquals("204 NO_CONTENT", response.getMessage());
    }

    @Test
    void shouldThrowException500WhenServerRespondedToLong() {
        //given
        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=25")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON_CONTENT_TYPE_VALUE)
                        .withBody("""
                                [1, 2, 3, 4, 5, 6, 82, 82, 83, 83, 86, 57, 10, 81, 53, 93, 50, 54, 31, 88, 15, 43, 79, 32, 43]
                                      """.trim()
                        )
                        .withFixedDelay(5000)));
        //when && then
        ResponseStatusException response = assertThrows(
                ResponseStatusException.class,
                () -> randomNumbersGenerable.generateSixRandomNumbers(25, 1, 99)
        );
        assertEquals("500 INTERNAL_SERVER_ERROR", response.getMessage());
    }

    @Test
    void shouldThrowException404WhenHttpServiceReturningNotFoundStatus() {
        // given
        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=25")
                .willReturn(WireMock.aResponse()
                        .withHeader(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON_CONTENT_TYPE_VALUE)
                        .withStatus(com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpStatus.SC_NOT_FOUND))
        );

        //when && then
        ResponseStatusException response = assertThrows(
                ResponseStatusException.class,
                () -> randomNumbersGenerable.generateSixRandomNumbers(25, 1, 99)
        );
        assertEquals("404 NOT_FOUND", response.getMessage());
    }

    @Test
    void shouldThrowException401WhenUserIsNotAuthorized(){
        //given
        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=25")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.UNAUTHORIZED.value())
                        .withHeader(CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON_CONTENT_TYPE_VALUE)));
        //when && then
        ResponseStatusException response = assertThrows(
                ResponseStatusException.class,
                () -> randomNumbersGenerable.generateSixRandomNumbers(25, 1, 99)
        );
        assertEquals("401 UNAUTHORIZED", response.getMessage());
    }
}


