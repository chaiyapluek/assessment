package com.kbtg.bootcamp.posttest.lottery;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kbtg.bootcamp.posttest.exception.DuplicateException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class LotteryService {
    
    private LotteryRepositoty lotteryRepositoty;

    public LotteryService(LotteryRepositoty lotteryRepositoty) {
        this.lotteryRepositoty = lotteryRepositoty;
    }

    public List<Lottery> getLotteries() {
        return lotteryRepositoty.findAll();
    }

    @Transactional
    public Lottery createLottery(@Valid Lottery lottery) {
        Optional<Lottery> tmp = lotteryRepositoty.findById(lottery.getId());
        if (tmp.isPresent()) {
            throw new DuplicateException("lottery " + lottery.getId() + " is already exist");
        }
        return lotteryRepositoty.save(lottery);
    }

}
