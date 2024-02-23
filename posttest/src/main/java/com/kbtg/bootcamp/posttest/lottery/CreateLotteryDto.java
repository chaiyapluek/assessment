package com.kbtg.bootcamp.posttest.lottery;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateLotteryDto(
    @NotNull
    @Size(min = 6, max = 6, message = "ticket must be 10 characters")
    String ticket,

    @NotNull
    @Positive
    Integer price,

    @NotNull
    @PostConstruct
    Integer amount
) {
    
}
