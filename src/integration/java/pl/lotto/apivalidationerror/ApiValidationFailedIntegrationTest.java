package pl.lotto.apivalidationerror;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.BaseIntegrationTest;
import pl.lotto.infrastructure.apicationvalidation.ApiValidationResponseDto;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiValidationFailedIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldReturn400BadRequestAndValidationMessageWhenInputNumbersAreEmpty() throws Exception {
        //given
        //when
        ResultActions performWithEmptyNumbers = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers": []
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON));
        //then
        MvcResult mvcResult = performWithEmptyNumbers.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationResponseDto response = objectMapper.readValue(json, ApiValidationResponseDto.class);
        assertAll(
                () -> Assertions.assertTrue(response.message().contains("inputNumber must be not empty")),
                () -> assertEquals(HttpStatus.BAD_REQUEST,response.status())
        );
    }

    @Test
    void shouldReturn400BadRequestAndValidationMessageWhenThereAreNoInputNumbers() throws Exception {
        //given
        //when
        ResultActions performPostWithOutInputNumbers = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON));
        //then
        MvcResult mvcResult = performPostWithOutInputNumbers.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationResponseDto response = objectMapper.readValue(json, ApiValidationResponseDto.class);
        assertAll(
                () -> Assertions.assertTrue(response.message().contains("inputNumber must be not empty")),
                () -> Assertions.assertTrue(response.message().contains("inputNumber must be not null")),
                () -> assertEquals(HttpStatus.BAD_REQUEST,response.status())
        );
    }

}
