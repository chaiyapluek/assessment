package com.kbtg.bootcamp.posttest.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kbtg.bootcamp.posttest.annotation.NumberString;
import com.kbtg.bootcamp.posttest.lottery.Lottery;



@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}/lotteries")
    public ResponseEntity<UserLotteriesReponse> getUser(
        @PathVariable("userId")
        @NumberString(min = 10, max = 10)
        String userId
    ){
        User user = userService.getUserLottery(userId);
        List<String> tickets = user.getLotteries().stream().map(Lottery::getId).collect(Collectors.toList());
        Integer count = user.getLotteries().size();
        Integer cost = user.getLotteries().stream().mapToInt(Lottery::getPrice).sum();
        return ResponseEntity.status(HttpStatus.OK).body(new UserLotteriesReponse(tickets, count, cost));
    }

    @PostMapping("/{userId}/lotteries/{ticketId}")
    public ResponseEntity<BuyLotteryResponse> buyLottery(
        @PathVariable("userId") 
        @NumberString(min = 10, max = 10)
        String userId,
        @PathVariable("ticketId")
        @NumberString(min = 6, max = 6)
        String ticketId
    ){
        Integer id = userService.buyTickey(userId, ticketId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BuyLotteryResponse(id.toString()));
    }

    @DeleteMapping("/{userId}/lotteries/{ticketId}")
    public ResponseEntity<DeleteUserTicketResponse> deleteLottery(
        @PathVariable("userId") 
        @NumberString(min = 10, max = 10)
        String userId,
        @PathVariable("ticketId") 
        @NumberString(min = 6, max = 6)
        String ticketId
    ){
        userService.deleteLottery(userId, ticketId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteUserTicketResponse(ticketId));
    }

}
