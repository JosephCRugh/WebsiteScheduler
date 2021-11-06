package com.gmail.josephcrugh.scheduler.schedule;

import com.gmail.josephcrugh.scheduler.registration.db.RegisteredUser;
import com.gmail.josephcrugh.scheduler.registration.RegisteredUsersService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class SchedulerController {

    private RegisteredUsersService registeredUsersService;

    @GetMapping("/schedule/search")
    public String presentSearchPage() {
        return "schedule/search";
    }

    @GetMapping("/schedule/search/lookup")
    public String findUserByEmailSearch(@RequestParam("email") final String email,
                                        Authentication authentication,
                                        Model model) {

        RegisteredUser user = (RegisteredUser) authentication.getPrincipal();

        String lowerCaseEmail = email.toLowerCase();

        // Prevent the user from searching themselves
        if (lowerCaseEmail.equals(user.getEmail())) {
            model.addAttribute("errorMessage", "Cannot schedule an appointment with yourself");
            return "schedule/search";
        }

        Optional<RegisteredUser> possibleUser = registeredUsersService.findUserByEmail(lowerCaseEmail);
        if (possibleUser.isEmpty()) {
            model.addAttribute("errorMessage",
                    String.format("User with the given email %s not found", email));
            return "schedule/search";
        }

        RegisteredUser searchedUser = possibleUser.get();

        model.addAttribute("foundUser", new ScheduleUser(
                searchedUser.getEmail(),
                searchedUser.getFirstName(),
                searchedUser.getLastName()
        ));
        model.addAttribute("selectTimeURL",
                "/schedule/select-time/" + searchedUser.getEmail());

        return "schedule/search";
    }

    @GetMapping("schedule/select-time/{email}")
    public String selectScheduleTime(@PathVariable String email) {

        System.out.println("Setting up an appointment with email: " + email);

        return "schedule/select-time";
    }
}
