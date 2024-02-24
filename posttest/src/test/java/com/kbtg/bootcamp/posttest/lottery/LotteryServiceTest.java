package com.kbtg.bootcamp.posttest.lottery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kbtg.bootcamp.posttest.exception.DuplicateException;

@ExtendWith(MockitoExtension.class)
public class LotteryServiceTest {

    @Mock
    private LotteryRepositoty lotteryRepositoty;

    private LotteryService lotteryService;

    @BeforeEach
    void setUp() {
        this.lotteryService = new LotteryService(lotteryRepositoty);
    }

    @Test
    @DisplayName("Given no lottery 000001 in database, create lottery 000001 should success")
    void testCreateLotterySuccess() {
        Lottery lottery = new Lottery();
        lottery.setId("000001");
        lottery.setAmount(100);
        lottery.setPrice(80);
        Optional<Lottery> empty = Optional.empty();

        when(lotteryRepositoty.findById("000001")).thenReturn(empty);
        when(lotteryRepositoty.save(any())).thenReturn(lottery);
        
        Lottery actual = lotteryService.createLottery(lottery);
        
        assertEquals("000001", actual.getId());
        assertEquals(100, actual.getAmount());
        assertEquals(80, actual.getPrice());
    }

    @Test
    @DisplayName("Given lottery 000001 in database, create lottery 000001 should fail")
    void testCreateLotteryDuplicateException() {
        Lottery lottery = new Lottery();
        lottery.setId("000001");
        lottery.setAmount(100);
        lottery.setPrice(80);
        Optional<Lottery> tmp = Optional.of(lottery);

        when(lotteryRepositoty.findById("000001")).thenReturn(tmp);
        
        Exception ex = assertThrows(DuplicateException.class, () -> {
            lotteryService.createLottery(lottery);
        });

        assertEquals("lottery 000001 is already exist", ex.getMessage());
    }
}
