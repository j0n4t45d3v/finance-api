package com.management.finance.user.dto;

import com.management.finance.user.User;

public record ReturnUser(
        Long id,
        String email
) {

    public static ReturnUser of(User user){
        return new ReturnUser(user.getId(), user.getEmail());
    }

}
