package com.gmail.josephcrugh.scheduler.registration;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserByEmailNotFoundException extends UsernameNotFoundException {

    private static final String USER_NOT_FOUND_MSG = "Could not find user with the given email %s";

    public UserByEmailNotFoundException(String email) {
        super(String.format(USER_NOT_FOUND_MSG, email));
    }
}
