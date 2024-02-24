package com.kbtg.bootcamp.posttest.lottery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@ExtendWith(SpringExtension.class)
public class LotteryControllerTest {

    MockMvc mockMvc;

    @Mock
    LotteryService lotteryService;

    LotteryController lotteryController;

    @BeforeEach
    void setUp() {
        this.lotteryController = new LotteryController(lotteryService);
        mockMvc = MockMvcBuilders.standaloneSetup(lotteryController).build();
    }

    @Test
    @DisplayName("Given user request /lotteries, get lotteries should success")
    void testGetLotteriesSuccess() throws Exception{
        when(lotteryService.getLotteries()).thenReturn(List.of(
            Lottery.builder().id("000001").amount(100).price(80).build(),
            Lottery.builder().id("000002").amount(200).price(160).build()
        ));
        mockMvc.perform(get("/lotteries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tickets[0]").value("000001"))
                .andExpect(jsonPath("$.tickets[1]").value("000002"));
    }
}
