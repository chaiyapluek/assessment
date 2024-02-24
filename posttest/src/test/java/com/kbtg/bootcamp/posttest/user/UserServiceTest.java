package com.kbtg.bootcamp.posttest.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kbtg.bootcamp.posttest.exception.InsufficientLotteryException;
import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepositoty;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    UserRepository userRepository;

    @Mock
    UserTicketReposiroty userTicketReposiroty;

    @Mock
    LotteryRepositoty lotteryRepositoty;

    UserService userService;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(userRepository, userTicketReposiroty, lotteryRepositoty);
    }

    @Test
    @DisplayName("Given user 1234567890 in database, get user 1234567890 should success")
    void testGetUserLotterySuccess() {
        String userId = "1234567890";
        User user = User.builder().id(userId).build();
        Optional<User> oUser = Optional.of(user);

        when(userRepository.findById(userId)).thenReturn(oUser);
    
        User actual = userService.getUserLottery(userId);

        assertEquals("1234567890", actual.getId());
    }

    @Test
    @DisplayName("Given user 1234567890 not in database, get user 1234567890 should throw NotFoundException")
    void testGetUserLotteryNotFoundException() {
        String userId = "1234567890";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
    
        Exception ex = assertThrows(NotFoundException.class, () -> {
            userService.getUserLottery(userId);
        });

        assertEquals("user 1234567890 not found", ex.getMessage());
    }
    
    @Test
    @DisplayName("Given lottery 000001 not in database, buy ticket 000001 should throw NotFoundException")
    void testBuyTicketTicketNotFound() {
        String userId = "1234567890";
        String ticketId = "000001";
        when(lotteryRepositoty.findById(ticketId)).thenReturn(Optional.empty());
    
        Exception ex = assertThrows(NotFoundException.class, () -> {
            userService.buyTickey(userId, ticketId);
        });

        assertEquals("ticket 000001 not found", ex.getMessage());
    }

    @Test
    @DisplayName("Given lottery 000001 amount 0 in database, buy ticket 000001 should throw InsufficientLotteryException")
    void testBuyTicketInsufficientLotteryException() {
        String userId = "1234567890";
        String ticketId = "000001";
        Lottery lottery = Lottery.builder().id(ticketId).amount(0).build();
        Optional<Lottery> oLottery = Optional.of(lottery);
        when(lotteryRepositoty.findById(ticketId)).thenReturn(oLottery);
    
        Exception ex = assertThrows(InsufficientLotteryException.class, () -> {
            userService.buyTickey(userId, ticketId);
        });

        assertEquals("insufficient lottery 000001 amount", ex.getMessage());
    }

    @Test
    @DisplayName("Given user 1234567890 not in database, buy ticket 000001 should success and user repository should call save 1 time and amount should be decrease by 1")
    void testBuyTicketNoUserInDatabaseSuccess() {
        String userId = "1234567890";
        String ticketId = "000001";
        Lottery lottery = Lottery.builder().id(userId).amount(1).build();
        Optional<Lottery> oLottery = Optional.of(lottery);

        when(lotteryRepositoty.findById(ticketId)).thenReturn(oLottery);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User newUser = User.builder().id(userId).build();
        when(userRepository.save(any())).thenReturn(newUser);
        
        UserTicket userTicket = UserTicket.builder().user_id(userId).ticket_id(ticketId).build();
        when(userTicketReposiroty.save(any())).thenReturn(userTicket);
        when(lotteryRepositoty.save(lottery)).thenReturn(lottery);
        
        userService.buyTickey(userId, ticketId);
        
        verify(userRepository, times(1)).save(any());
        assertEquals(0, lottery.getAmount());
    }

    @Test
    @DisplayName("Given user 1234567890 in database, buy ticket 000001 should success and user repository should not call save and amount should be decrease by 1")
    void testBuyTicketHaveUserInDatabaseSuccess() {
        String userId = "1234567890";
        String ticketId = "000001";
        Lottery lottery = Lottery.builder().id(userId).amount(1).build();
        Optional<Lottery> oLottery = Optional.of(lottery);

        User user = User.builder().id(userId).build();
        Optional<User> oUser = Optional.of(user);

        when(lotteryRepositoty.findById(ticketId)).thenReturn(oLottery);
        when(userRepository.findById(userId)).thenReturn(oUser);
        
        UserTicket userTicket = UserTicket.builder().user_id(userId).ticket_id(ticketId).build();
        when(userTicketReposiroty.save(any())).thenReturn(userTicket);
        when(lotteryRepositoty.save(lottery)).thenReturn(lottery);
        
        userService.buyTickey(userId, ticketId);
        
        verify(userRepository, never()).save(any());
        assertEquals(0, lottery.getAmount());
    }

    @Test
    @DisplayName("Given user 1234567890 and ticket 000001 not in database, delete ticket 000001 should throw NotFoundException")
    void testDeleteTicketNotFound() {
        String userId = "1234567890";
        String ticketId = "000001";
        when(userTicketReposiroty.findByUserIdAndTicketId(userId, ticketId)).thenReturn(null);
    
        Exception ex = assertThrows(NotFoundException.class, () -> {
            userService.deleteLottery(userId, ticketId);
        });

        assertEquals("ticket 000001 of user 1234567890 not found", ex.getMessage());
    }

    @Test
    @DisplayName("Given user 1234567890 and ticket 000001 in database, delete ticket 000001 should success and user ticket repository should call delete 1 time")
    void testDeleteTicketSuccess() {
        String userId = "1234567890";
        String ticketId = "000001";
        List<UserTicket> userTicket = new ArrayList<>(
            List.of(
                UserTicket.builder().user_id(userId).ticket_id(ticketId).build()
            )
        );
        when(userTicketReposiroty.findByUserIdAndTicketId(userId, ticketId)).thenReturn(userTicket);
        
        userService.deleteLottery(userId, ticketId);
        
        verify(userTicketReposiroty, times(1)).delete(any());
    }
}
