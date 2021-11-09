package com.gmail.josephcrugh.scheduler.schedule;

import com.gmail.josephcrugh.scheduler.schedule.db.AvailableDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalTime;

@Controller
public class AvailableTimesController extends ScheduleDisplayController {

    @GetMapping("/schedule/create")
    public String showAddTimePage(Authentication authentication, Model model) {
        sendSchedule(authentication, model, ScheduleDisplayState.EDITING);
        return "/schedule/create";
    }

    @PostMapping("/schedule/create/add")
    public String addTime(@Valid @ModelAttribute("availableDateTime") AvailableDateTime availableDateTime,
                          Authentication authentication,
                          final BindingResult bindingResult,
                          Model model) {

        if (bindingResult.hasErrors()) {
            return "/schedule/create :: add-form-fragment";
        }

        LocalTime startTime = availableDateTime.getStartTime();
        LocalTime endTime = availableDateTime.getEndTime();
        if (startTime.isAfter(endTime)) {
            bindingResult.addError(new FieldError("availableDateTime", "endTime",
                    "Start time must come first"));
            return "/schedule/create :: add-form-fragment";
        }

        if (!availableTimesService.fitsInSchedule(availableDateTime, authentication)) {
            bindingResult.addError(new FieldError("availableDateTime", "endTime",
                    "Conflicts with schedule"));
            return "/schedule/create :: add-form-fragment";
        }

        availableTimesService.addAvailableTime(availableDateTime, authentication);

        sendSchedule(authentication, model, ScheduleDisplayState.EDITING);
        return "/schedule/time-sheet-fragments :: time-sheet-fragment";
    }

    @PostMapping("/schedule/create/remove/{timeId}")
    public String removeTime(@PathVariable("timeId") Long timeId,
                             Authentication authentication,
                             Model model) {

        availableTimesService.removeAvailableTime(timeId, authentication);

        sendSchedule(authentication, model, ScheduleDisplayState.EDITING);
        return "/schedule/time-sheet-fragments :: time-sheet-fragment";
    }
}
