package pl.lotto.cache.redis;

import org.awaitility.Durations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import pl.lotto.BaseIntegrationTest;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.domain.resultannouncer.ResultAnnouncerFacade;
import pl.lotto.domain.resultchecker.ResultCheckerFacade;
import pl.lotto.domain.resultchecker.ResultCheckerNotFoundException;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


public class RedisCacheIntegrationTest extends BaseIntegrationTest {

    @Container
    private static final GenericContainer<?> REDIS;

    @SpyBean
    ResultAnnouncerFacade resultAnnouncerFacade;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    ResultCheckerFacade resultCheckerFacade;

    static {
        REDIS = new GenericContainer<>("redis").withExposedPorts(6379);
        REDIS.start();
    }


    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.redis.port", () -> REDIS.getFirstMappedPort().toString());
        registry.add("spring.cache.type", () -> "redis");
        registry.add("spring.cache.redis.time-to-live", () -> "PT1S");
    }

    @Test
    void shouldSaveResultResponseAndThenInvalidateByTimeToLive() throws Exception {
        //step 1: user made POST /inputNumbers with 6 numbers (1, 2, 3, 4, 5, 6) at 05-07-2023 11:00
        //given
        ResultActions performPostWithInputNumbers = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers": [1,2,3,4,5,6]
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON));
        //when && then
        String json = performPostWithInputNumbers.andReturn()
                .getResponse()
                .getContentAsString();
        NumberReceiverResponseDto response = objectMapper.readValue(json, NumberReceiverResponseDto.class);
        String ticketId = response.ticketDto().ticketId();


        //step 2: 3 days and 55 minutes passed, and it is 5 minute before draw (08.07.2023 11:55), and
        //system generated result for TicketId with draw date 08.07.2023 12:00, and saved it
        //given && when && then
        clock.plusDaysAndMinutes(3, 55);
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


        //step 4: should save to cache result response
        //given
        mockMvc.perform(get("/results/" + ticketId));
        //when && then
        verify(resultAnnouncerFacade, times(1)).checkWinner(ticketId);
        assertThat(cacheManager.getCacheNames().contains("checkWinner")).isTrue();


        //step 5: cache should be invalidated
        //given && when && then
        await()
                .atMost(Duration.ofSeconds(4))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                    mockMvc.perform(get("/results/" + ticketId));
                    verify(resultAnnouncerFacade, times(2)).checkWinner(ticketId);
                });

    }
}
