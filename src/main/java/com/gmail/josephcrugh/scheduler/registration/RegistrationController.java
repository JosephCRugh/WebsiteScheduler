package com.gmail.josephcrugh.scheduler.registration;

import com.gmail.josephcrugh.scheduler.registration.db.RegisteredUser;
import com.gmail.josephcrugh.scheduler.registration.db.RegisteredUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class RegistrationController {

    private final RegisteredUsersService registeredUsersService;

    @GetMapping("/register")
    public String presentRegisterPage(RegisteredUser user) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid RegisteredUser user,
                           BindingResult result) {

        if (result.hasErrors()) {
            // Return back to the register page so they can try again
            return "register";
        }

        // Removing case sensitivity
        user.setEmail(user.getEmail().toLowerCase());

        if (registeredUsersService.userExistByEmail(user.getEmail())) {
            result.addError(new FieldError("user", "email", "Email Taken"));
            return "register";
        }

        registeredUsersService.storeNewUser(user);

        return "login";
    }
}
