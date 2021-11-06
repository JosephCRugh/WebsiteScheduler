package com.gmail.josephcrugh.scheduler.schedule;

import com.gmail.josephcrugh.scheduler.registration.db.RegisteredUser;
import com.gmail.josephcrugh.scheduler.schedule.db.AvailableDateTime;
import com.gmail.josephcrugh.scheduler.schedule.db.AvailableTimesRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
@AllArgsConstructor
public class AvailableTimesService {

    private final AvailableTimesRepository availableTimesRepository;

    public void addAvailableTime(AvailableDateTime availableDateTime,
                                 Authentication authentication) {

        availableDateTime.setUser((RegisteredUser) authentication.getPrincipal());
        availableTimesRepository.save(availableDateTime);

    }

    public List<AvailableDateTime> getOrderedDaySchedule(DayOfWeek day, Authentication authentication) {
        RegisteredUser user = (RegisteredUser) authentication.getPrincipal();
        return availableTimesRepository.findAllByUser(user, day);
    }

}
