package com.kbtg.bootcamp.posttest.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.kbtg.bootcamp.posttest.exception.InsufficientLotteryException;
import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.security.SecurityConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    @DisplayName("Given user request to GET /users/{userId}/lotteries and userId is 12345, the status code should be 400")
    void testGetUserLotteriesUserIdNot10DigitsLong() throws Exception {
        when(userService.getUserLottery(any())).thenReturn(null);
        mockMvc.perform(get("/users/12345/lotteries"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("userId must be 10 characters long."));
    }

    @Test
    @DisplayName("Given user request to GET /users/{userId}/lotteries and userId is a234567890, the status code should be 400")
    void testGetUserLotteriesUserIdNotANumber() throws Exception {
        when(userService.getUserLottery(any())).thenReturn(null);
        mockMvc.perform(get("/users/a234567890/lotteries"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("userId must be a number."));
    }

    @Test
    @DisplayName("Given user request to GET /users/{userId}/lotteries and userId is 1234567890, the status code should be 200")
    void testGetUserLotteriesSuccess() throws Exception {
        String userId = "1234567890";
        User user = new User();
        user.setId(userId);
        user.setLotteries(
            List.of(
                Lottery.builder().id("000001").amount(100).price(80).build(),
                Lottery.builder().id("000002").amount(200).price(160).build()
            )
        );

        when(userService.getUserLottery(userId)).thenReturn(user);

        mockMvc.perform(get("/users/1234567890/lotteries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tickets[0]").value("000001"))
                .andExpect(jsonPath("$.tickets[1]").value("000002"))
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.cost").value(240));
    }

    @Test
    @DisplayName("Given user request to POST /users/{userId}/lotteries/{ticketId}, userId is 12345 and ticketId is 000001, the status code should be 400")
    void testBuyLotteryUserIdNot10DigitLong() throws Exception {
        mockMvc.perform(post("/users/12345/lotteries/000001"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("userId must be 10 characters long."));
    }

    @Test
    @DisplayName("Given user request to POST /users/{userId}/lotteries/{ticketId}, userId is a234567890 and ticketId is 000001, the status code should be 400")
    void testBuyLotteryUserIdNotANumber() throws Exception {
        mockMvc.perform(post("/users/a234567890/lotteries/000001"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("userId must be a number."));
    }

    @Test
    @DisplayName("Given user request to POST /users/{userId}/lotteries/{ticketId}, userId is 1234567890 and ticketId is 00001, the status code should be 400")
    void testBuyLotteryTicketIdNot6DigitLong() throws Exception {
        mockMvc.perform(post("/users/1234567890/lotteries/00001"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ticketId must be 6 characters long."));
    }

    @Test
    @DisplayName("Given user request to POST /users/{userId}/lotteries/{ticketId}, userId is 1234567890 and ticketId is a00001, the status code should be 400")
    void testBuyLotteryTicketIdNotANumber() throws Exception {
        mockMvc.perform(post("/users/1234567890/lotteries/a00001"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ticketId must be a number."));
    }

    @Test
    @DisplayName("Given user request to POST /users/{userId}/lotteries/{ticketId}, userId is 1234567890 and ticketId is 000001, the status code should be 201")
    void testBuyLotterySuccess() throws Exception {
        String userId = "1234567890";
        String ticketId = "000001";
        when(userService.buyTickey(userId, ticketId)).thenReturn(1);

        mockMvc.perform(post("/users/1234567890/lotteries/000001"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    @DisplayName("Given user request to POST /users/{userId}/lotteries/{ticketId}, userId is 1234567890 and ticketId is 000001 but ticket are not found, the status code should be 404")
    void testBuyLotteryTicketNotFound() throws Exception {
        String userId = "1234567890";
        String ticketId = "000001";
        when(userService.buyTickey(userId, ticketId)).thenThrow(new NotFoundException("ticket 000001 not found"));

        mockMvc.perform(post("/users/1234567890/lotteries/000001"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("ticket 000001 not found"));
    }

    @Test
    @DisplayName("Given user request to POST /users/{userId}/lotteries/{ticketId}, userId is 1234567890 and ticketId is 000001 but ticket are insufficient, the status code should be 404")
    void testBuyLotteryInsufficientLotteryException() throws Exception {
        String userId = "1234567890";
        String ticketId = "000001";
        when(userService.buyTickey(userId, ticketId)).thenThrow(new InsufficientLotteryException("insufficient lottery 000001 amount"));

        mockMvc.perform(post("/users/1234567890/lotteries/000001"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("insufficient lottery 000001 amount"));
    }

    @Test
    @DisplayName("Given user request to DELETE /users/{userId}/lotteries/{ticketId}, userId is 12345 and ticketId is 000001, the status code should be 400")
    void testDeleteLotteryUserIdNot10DigitLong() throws Exception {
        mockMvc.perform(delete("/users/12345/lotteries/000001"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("userId must be 10 characters long."));
    }

    @Test
    @DisplayName("Given user request to DELETE /users/{userId}/lotteries/{ticketId}, userId is a234567890 and ticketId is 000001, the status code should be 400")
    void testDeleteLotteryUserIdNotANumber() throws Exception {
        mockMvc.perform(delete("/users/a234567890/lotteries/000001"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("userId must be a number."));
    }

    @Test
    @DisplayName("Given user request to DELETE /users/{userId}/lotteries/{ticketId}, userId is 1234567890 and ticketId is 00001, the status code should be 400")
    void testDeleteLotteryTicketIdNot6DigitLong() throws Exception {
        mockMvc.perform(post("/users/1234567890/lotteries/00001"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ticketId must be 6 characters long."));
    }

    @Test
    @DisplayName("Given user request to DELETE /users/{userId}/lotteries/{ticketId}, userId is 1234567890 and ticketId is a00001, the status code should be 400")
    void testDeleteLotteryTicketIdNotANumber() throws Exception {
        mockMvc.perform(delete("/users/1234567890/lotteries/a00001"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ticketId must be a number."));
    }

    @Test
    @DisplayName("Given user request to DELETE /users/{userId}/lotteries/{ticketId}, userId is 1234567890, ticketId is 000001 but user with that ticket are not found, the status code should be 404")
    void testDeleteLotteryUserTicketNotFound() throws Exception {
        String userId = "1234567890";
        String ticketId = "000001";
        when(userService.deleteLottery(userId, ticketId)).thenThrow(new NotFoundException("ticket 000001 of user 1234567890 not found"));

        mockMvc.perform(delete("/users/1234567890/lotteries/000001"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("ticket 000001 of user 1234567890 not found"));
    }

    @Test
    @DisplayName("Given user request to DELETE /users/{userId}/lotteries/{ticketId}, userId is 1234567890 and ticketId is 000001, the status code should be 200")
    void testDeleteLotterySuccess() throws Exception {
        String userId = "1234567890";
        String ticketId = "000001";
        when(userService.deleteLottery(userId, ticketId)).thenReturn(ticketId);

        mockMvc.perform(delete("/users/1234567890/lotteries/000001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticket").value("000001"));
    }
}
