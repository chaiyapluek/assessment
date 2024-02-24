package com.kbtg.bootcamp.posttest.lottery;

import java.util.List;

import com.kbtg.bootcamp.posttest.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lottery")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lottery {
    @Id
    private String id;

    @NotNull
    @Positive
    private Integer price;

    @NotNull
    @PositiveOrZero
    private Integer amount;

    @ManyToMany(mappedBy = "lotteries")
    private List<User> users;
}
