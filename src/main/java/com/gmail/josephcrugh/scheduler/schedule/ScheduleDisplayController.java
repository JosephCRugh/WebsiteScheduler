package com.gmail.josephcrugh.scheduler.schedule;

import com.gmail.josephcrugh.scheduler.registration.db.RegisteredUser;
import com.gmail.josephcrugh.scheduler.schedule.db.AvailableDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.time.DayOfWeek;
import java.util.List;

public abstract class ScheduleDisplayController {

    @Autowired
    protected AvailableTimesService availableTimesService;

    private void constructDayScheduleModel(String name, DayOfWeek day, Model model, RegisteredUser user) {
        List<AvailableDateTime> mondaySchedule =
                availableTimesService.getOrderedDaySchedule(day, user);
        model.addAttribute(name, mondaySchedule);
    }

    public void sendSchedule(RegisteredUser user, Model model, ScheduleDisplayState state) {
        constructDayScheduleModel("mondaySchedule", DayOfWeek.MONDAY, model, user);
        constructDayScheduleModel("tuesdaySchedule", DayOfWeek.TUESDAY, model, user);
        constructDayScheduleModel("wednesdaySchedule", DayOfWeek.WEDNESDAY, model, user);
        constructDayScheduleModel("thursdaySchedule", DayOfWeek.THURSDAY, model, user);
        constructDayScheduleModel("fridaySchedule", DayOfWeek.FRIDAY, model, user);
        constructDayScheduleModel("saturdaySchedule", DayOfWeek.SATURDAY, model, user);
        constructDayScheduleModel("sundaySchedule", DayOfWeek.SUNDAY, model, user);
        model.addAttribute("scheduleState", state);
    }

    public void sendSchedule(Authentication authentication, Model model, ScheduleDisplayState state) {
        sendSchedule((RegisteredUser) authentication.getPrincipal(), model, state);
    }

}
