package edu.eci.arsw.parallelism.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PiDigitsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSequentialAndParallelProduceSameResults() throws Exception {
        String sequentialResponse = mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "20")
                        .param("strategy", "sequential"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String parallelResponse = mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "20")
                        .param("strategy", "threads")
                        .param("threads", "4"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assert sequentialResponse.equals(parallelResponse);
    }

    @Test
    void testEndToEndCalculationWithDefaultStrategy() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start").value(0))
                .andExpect(jsonPath("$.count").value(10))
                .andExpect(jsonPath("$.digits").isString())
                .andExpect(jsonPath("$.digits").value("243F6A8885"));
    }

    @Test
    void testEndToEndCalculationWithThreadsStrategy() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "10")
                        .param("strategy", "threads")
                        .param("threads", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start").value(0))
                .andExpect(jsonPath("$.count").value(10))
                .andExpect(jsonPath("$.digits").value("243F6A8885"));
    }

    @Test
    void testDifferentThreadCountsProduceSameResults() throws Exception {
        String twoThreads = mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "50")
                        .param("strategy", "threads")
                        .param("threads", "2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String fourThreads = mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "50")
                        .param("strategy", "threads")
                        .param("threads", "4"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String eightThreads = mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "50")
                        .param("strategy", "threads")
                        .param("threads", "8"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assert twoThreads.equals(fourThreads);
        assert fourThreads.equals(eightThreads);
    }

    @Test
    void testValidationIntegration() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "-1")
                        .param("count", "10"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "-5"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "10")
                        .param("threads", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLargeCalculationIntegration() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "100")
                        .param("strategy", "threads")
                        .param("threads", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(100))
                .andExpect(jsonPath("$.digits").isString());
    }

    @Test
    void testDifferentStartPositions() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.digits").value("243F6"));

        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "5")
                        .param("count", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.digits").value("A8885"));

        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "10")
                        .param("count", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.digits").isString());
    }

    @Test
    void testConsecutiveRequestsProduceSameResults() throws Exception {
        String firstRequest = mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "30")
                        .param("strategy", "threads")
                        .param("threads", "4"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String secondRequest = mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "30")
                        .param("strategy", "threads")
                        .param("threads", "4"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assert firstRequest.equals(secondRequest);
    }

    @Test
    void testThreadsStrategyWithOneThread() throws Exception {
        String sequential = mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "15")
                        .param("strategy", "sequential"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String oneThread = mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "15")
                        .param("strategy", "threads")
                        .param("threads", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assert sequential.equals(oneThread);
    }

    @Test
    void testMoreThreadsThanDigits() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "5")
                        .param("strategy", "threads")
                        .param("threads", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5))
                .andExpect(jsonPath("$.digits").value("243F6"));
    }

    @Test
    void testOddNumberOfDigitsWithMultipleThreads() throws Exception {
        String sequential = mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "13")
                        .param("strategy", "sequential"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String parallel = mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "13")
                        .param("strategy", "threads")
                        .param("threads", "5"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assert sequential.equals(parallel);
    }

    @Test
    void testNullThreadsDefaultsToAvailableProcessors() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "20")
                        .param("strategy", "threads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(20))
                .andExpect(jsonPath("$.digits").isString());
    }

    @Test
    void testCaseInsensitiveStrategyName() throws Exception {
        String lowercase = mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "10")
                        .param("strategy", "threads")
                        .param("threads", "2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String uppercase = mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "10")
                        .param("strategy", "THREADS")
                        .param("threads", "2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assert lowercase.equals(uppercase);
    }

    @Test
    void testLargeStartPosition() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "1000")
                        .param("count", "10")
                        .param("strategy", "threads")
                        .param("threads", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start").value(1000))
                .andExpect(jsonPath("$.count").value(10))
                .andExpect(jsonPath("$.digits").isString());
    }

    @Test
    void testResponseFormat() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.start").isNumber())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.digits").isString());
    }
}
