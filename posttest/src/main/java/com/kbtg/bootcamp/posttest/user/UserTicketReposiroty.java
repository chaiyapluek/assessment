package com.kbtg.bootcamp.posttest.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserTicketReposiroty extends JpaRepository<UserTicket, Integer> {
    
    @Query("SELECT u FROM UserTicket u WHERE u.user_id = ?1 AND u.ticket_id = ?2")
    List<UserTicket> findByUserIdAndTicketId(String userId, String ticketId);

}
