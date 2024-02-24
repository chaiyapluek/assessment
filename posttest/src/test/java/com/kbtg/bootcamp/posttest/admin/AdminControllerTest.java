package com.kbtg.bootcamp.posttest.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kbtg.bootcamp.posttest.exception.DuplicateException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import com.kbtg.bootcamp.posttest.security.SecurityConfig;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
public class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LotteryService lotteryService;

    @Test
    @DisplayName("Given no authorization, create lottery should return 401")
    void testCreateLotteryUnauthorized() throws Exception {
        String body =
        """
        {
            "ticket": "000001",
            "price": 80,
            "amount": 100
        }        
        """;
        mockMvc.perform(post("/admin/lotteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "ADMIN")
    @DisplayName("Given authorization, create lottery should return 201")
    void testCreateLotterySuccess() throws Exception {
        String body =
        """
        {
            "ticket": "000001",
            "price": 80,
            "amount": 100
        }        
        """;
        when(lotteryService.createLottery(any())).thenReturn(Lottery.builder().id("000001").amount(100).price(80).build());
        mockMvc.perform(post("/admin/lotteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticket").value("000001"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "ADMIN")
    @DisplayName("Given authorization, create lottery with ticket 00001 should return 400")
    void testCreateLotteryInvalidTicket() throws Exception {
        String body =
        """
        {
            "ticket": "00001",
            "price": 80,
            "amount": 100
        }        
        """;
        mockMvc.perform(post("/admin/lotteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ticket must be 6 characters long."));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "ADMIN")
    @DisplayName("Given authorization, create lottery with ticket a00001 should return 400")
    void testCreateLotteryInvalidTicket2() throws Exception {
        String body =
        """
        {
            "ticket": "a00001",
            "price": 80,
            "amount": 100
        }        
        """;
        mockMvc.perform(post("/admin/lotteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ticket must be a number."));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "ADMIN")
    @DisplayName("Given authorization, create lottery without ticket should return 400")
    void testCreateLotteryInvalidTicket3() throws Exception {
        String body =
        """
        {
            "price": 80,
            "amount": 100
        }        
        """;
        mockMvc.perform(post("/admin/lotteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ticket cannot be null or empty."));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "ADMIN")
    @DisplayName("Given authorization, create lottery with price -1 should return 400")
    void testCreateLotteryWithNegativePrice() throws Exception {
        String body =
        """
        {
            "ticket": "000001",
            "price": -1,
            "amount": 100
        }        
        """;
        mockMvc.perform(post("/admin/lotteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("price must be greater than 0"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "ADMIN")
    @DisplayName("Given authorization, create lottery without price should return 400")
    void testCreateLotteryWithoutPrice() throws Exception {
        String body =
        """
        {
            "ticket": "000001",
            "amount": 100
        }        
        """;
        mockMvc.perform(post("/admin/lotteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("price must not be null"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "ADMIN")
    @DisplayName("Given authorization, create lottery with amount -1 should return 400")
    void testCreateLotteryWithNegativeAmount() throws Exception {
        String body =
        """
        {
            "ticket": "000001",
            "price": 80,
            "amount": -1
        }        
        """;
        mockMvc.perform(post("/admin/lotteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("amount must be greater than 0"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "ADMIN")
    @DisplayName("Given authorization, create lottery without amount should return 400")
    void testCreateLotteryWithoutAmount() throws Exception {
        String body =
        """
        {
            "ticket": "000001",
            "price": 80
        }        
        """;
        mockMvc.perform(post("/admin/lotteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("amount must not be null"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "ADMIN")
    @DisplayName("Given authorization, create a duplicate lottery should return 400")
    void testCreateLotteryDuplicate() throws Exception {
        String body =
        """
        {
            "ticket": "000001",
            "price": 80,
            "amount": 100
        }        
        """;
        when(lotteryService.createLottery(any())).thenThrow(new DuplicateException("lottery 000001 is already exists"));
        mockMvc.perform(post("/admin/lotteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("lottery 000001 is already exists"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = "ADMIN")
    @DisplayName("Given authorization, create lottery runtime exception should return 500")
    void testCreateLotteryRunTimeException() throws Exception {
        String body =
        """
        {
            "ticket": "000001",
            "price": 80,
            "amount": 100
        }        
        """;
        when(lotteryService.createLottery(any())).thenThrow(new RuntimeException("any exception"));
        mockMvc.perform(post("/admin/lotteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("internal server error"));
    }
}
