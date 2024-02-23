package com.kbtg.bootcamp.posttest.user;

import java.util.List;

public record UserLotteriesReponse(
    List<String> tickets,
    Integer count,
    Integer cost
) {
    
}
