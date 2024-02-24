package com.kbtg.bootcamp.posttest.user;

import java.util.List;

import com.kbtg.bootcamp.posttest.lottery.Lottery;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Size(min = 10, max = 10, message = "user id must be 10 characters")
    private String id;

    @ManyToMany
    @JoinTable(
        name = "user_ticket",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "ticket_id")
    )
    private List<Lottery> lotteries;
}
