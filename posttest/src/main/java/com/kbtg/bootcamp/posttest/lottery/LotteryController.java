package com.kbtg.bootcamp.posttest.lottery;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lotteries")
public class LotteryController {
    
    private LotteryService lotteryService;

    public LotteryController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @GetMapping("")
    public ResponseEntity<LotteriesResponse> getLotteries() {
        List<String> lotteries = lotteryService.getLotteries().stream().map(lottery -> lottery.getId()).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new LotteriesResponse(lotteries));
    }
}
