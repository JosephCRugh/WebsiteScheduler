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
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Controller
@Transactional
public class AvailableTimesController {

    @Autowired
    private AvailableTimesService availableTimesService;

    private void constructDayScheduleModel(String name, DayOfWeek day, Model model, Authentication authentication) {
        List<AvailableDateTime> mondaySchedule =
                availableTimesService.getOrderedDaySchedule(day, authentication);
        model.addAttribute(name, mondaySchedule);
    }

    private void sendSchedule(Authentication authentication, Model model) {
        constructDayScheduleModel("mondaySchedule", DayOfWeek.MONDAY, model, authentication);
        constructDayScheduleModel("tuesdaySchedule", DayOfWeek.TUESDAY, model, authentication);
        constructDayScheduleModel("wednesdaySchedule", DayOfWeek.WEDNESDAY, model, authentication);
        constructDayScheduleModel("thursdaySchedule", DayOfWeek.THURSDAY, model, authentication);
        constructDayScheduleModel("fridaySchedule", DayOfWeek.FRIDAY, model, authentication);
        constructDayScheduleModel("saturdaySchedule", DayOfWeek.SATURDAY, model, authentication);
        constructDayScheduleModel("sundaySchedule", DayOfWeek.SUNDAY, model, authentication);
    }

    @GetMapping("/schedule/create")
    public String showAddTimePage(Authentication authentication, Model model) {
        sendSchedule(authentication, model);
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
                    "Ending time must come first"));
            return "/schedule/create :: add-form-fragment";
        }

        // TODO: need to check to make sure the time-slot is conflicting

        availableTimesService.addAvailableTime(availableDateTime, authentication);

        sendSchedule(authentication, model);
        return "/schedule/create :: time-sheet-fragment";
    }

    @PostMapping("/schedule/create/remove/{timeId}")
    public String removeTime(@PathVariable("timeId") Long timeId,
                             Authentication authentication,
                             Model model) {

        availableTimesService.removeAvailableTime(timeId, authentication);

        sendSchedule(authentication, model);
        return "/schedule/create :: time-sheet-fragment";
    }
}
