package com.gmail.josephcrugh.scheduler.schedule;

import com.gmail.josephcrugh.scheduler.registration.RegisteredUsersService;
import com.gmail.josephcrugh.scheduler.registration.db.RegisteredUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class SchedulerController extends ScheduleDisplayController {

    @Autowired
    private RegisteredUsersService registeredUsersService;

    @GetMapping("/schedule/search")
    public String presentSearchPage() {
        return "schedule/search";
    }

    @GetMapping("/schedule/search/lookup")
    public String findUserByEmailSearch(@ModelAttribute("email") final String email,
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

    @GetMapping("/schedule/select-time/{email}")
    public String selectScheduleTime(@PathVariable String email, Model model) {

        System.out.println("Fetching the schedule by email: " + email);

        Optional<RegisteredUser> possibleScheduleWithUser = registeredUsersService.findUserByEmail(email.toLowerCase());
        if (possibleScheduleWithUser.isEmpty()) {
            // TODO: report error with there not being a user by that email
            return "schedule/select-time";
        }
        RegisteredUser scheduleWithUser = possibleScheduleWithUser.get();

        model.addAttribute("email", email);
        sendSchedule(scheduleWithUser, model, ScheduleDisplayState.SELECTING_TIME);
        return "schedule/select-time";
    }

    @GetMapping("/schedule/confirm-schedule-select/{email}/{timeId}")
    public String confirmScheduleSelect(@PathVariable String email, @PathVariable Long timeId) {
        System.out.println("Confirming with email: " + email);
        System.out.println("Confirming with timeID: " + timeId);
        return "schedule/confirmation-page";
    }
}
