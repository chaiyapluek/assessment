package com.kbtg.bootcamp.posttest.user;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kbtg.bootcamp.posttest.exception.InsufficientLotteryException;
import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepositoty;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    
    private UserRepository userRepository;
    private UserTicketReposiroty userTicketReposiroty;
    private LotteryRepositoty lotteryRepositoty;

    public UserService(UserRepository userRepository, UserTicketReposiroty userTicketReposiroty, LotteryRepositoty lotteryRepositoty) {
        this.userRepository = userRepository;
        this.userTicketReposiroty = userTicketReposiroty;
        this.lotteryRepositoty = lotteryRepositoty;
    }

    public User getUserLottery(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user " + userId + " not found"));
        return user;
    }

    @Transactional
    public Integer buyTickey(String userId, String ticketId) {
        Lottery lottery = lotteryRepositoty.findById(ticketId).orElseThrow(() -> new NotFoundException("ticket " + ticketId + " not found"));
        if (lottery.getAmount() <= 0) {
            throw new InsufficientLotteryException("insufficient lottery " + ticketId + " amount");
        }
        User user = userRepository.findById(userId).orElse(null);
        System.out.println("user: " + user);
        if (user == null) {
            User newUser = new User();
            newUser.setId(userId);
            userRepository.save(newUser);
        }
        UserTicket userTicket = new UserTicket();
        userTicket.setUser_id(userId);
        userTicket.setTicket_id(ticketId);
        userTicketReposiroty.save(userTicket);

        lottery.setAmount(lottery.getAmount() - 1);
        lotteryRepositoty.save(lottery);

        return userTicket.getId();
    }

    @Transactional
    public String deleteLottery(String userId, String ticketId) {
        List<UserTicket> userTicket = userTicketReposiroty.findByUserIdAndTicketId(userId, ticketId);
        if (userTicket == null || userTicket.size() == 0) {
            throw new NotFoundException("ticket " + ticketId + " of user " + userId + " not found");
        }
        userTicket.forEach(o -> userTicketReposiroty.delete(o));
        return ticketId;
    }
}
