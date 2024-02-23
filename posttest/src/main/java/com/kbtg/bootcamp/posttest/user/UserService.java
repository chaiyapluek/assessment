package com.kbtg.bootcamp.posttest.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        User user = userRepository.findById(userId).orElse(null);
        return user;
    }

    @Transactional
    public Integer buyTickey(String userId, String ticketId) {
        lotteryRepositoty.findById(ticketId).orElseThrow(() -> new IllegalArgumentException("ticket" + ticketId + " not found"));

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            User newUser = new User();
            newUser.setId(userId);
            userRepository.save(newUser);
        }
        UserTicket userTicket = new UserTicket();
        userTicket.setUser_id(userId);
        userTicket.setTicket_id(ticketId);
        userTicketReposiroty.save(userTicket);
        return userTicket.getId();
    }

    @Transactional
    public void deleteLottery(String userId, String ticketId) {
        List<UserTicket> userTicket = userTicketReposiroty.findByUserIdAndTicketId(userId, ticketId);
        if (userTicket == null || userTicket.size() == 0) {
            throw new IllegalArgumentException("ticket" + ticketId + " not found");
        }
        userTicket.forEach(o -> userTicketReposiroty.delete(o));
    }
}
