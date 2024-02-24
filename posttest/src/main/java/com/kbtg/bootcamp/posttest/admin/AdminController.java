package com.kbtg.bootcamp.posttest.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kbtg.bootcamp.posttest.lottery.CreateLotteryDto;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.CreateLotteryResponse;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    private LotteryService lotteryService;

    public AdminController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @PostMapping("/lotteries")
    public ResponseEntity<CreateLotteryResponse> createLottery(@RequestBody @Valid CreateLotteryDto request){
        Lottery newLottery = Lottery.builder()
                .id(request.ticket())
                .price(request.price())
                .amount(request.amount())
                .build();
        lotteryService.createLottery(newLottery);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateLotteryResponse(newLottery.getId()));
    }
}
