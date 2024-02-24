package com.kbtg.bootcamp.posttest.lottery;

import com.kbtg.bootcamp.posttest.annotation.NumberString;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateLotteryDto(
    @NumberString(min = 6, max = 6)
    String ticket,

    @NotNull
    @Positive
    Integer price,

    @NotNull
    @Positive
    Integer amount
) {
    
}
