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

    public void removeAvailableTime(Long timeId,
                                    Authentication authentication) {
        RegisteredUser user = (RegisteredUser) authentication.getPrincipal();
        availableTimesRepository.deleteByIdAndUser(timeId, user);
    }

    public List<AvailableDateTime> getOrderedDaySchedule(DayOfWeek day, Authentication authentication) {
        RegisteredUser user = (RegisteredUser) authentication.getPrincipal();
        return availableTimesRepository.findAllByUser(user, day);
    }

    public boolean fitsInSchedule(AvailableDateTime time, Authentication authentication) {
        List<AvailableDateTime> daySchedule = getOrderedDaySchedule(time.getDay(), authentication);
        // Special case if there are no times
        if (daySchedule.isEmpty()) return true;
        // Special case if it is the first time
        if (time.getEndTime().isBefore(daySchedule.get(0).getStartTime())) return true;

        for (int i = 0; i < daySchedule.size(); i++) {
            AvailableDateTime prevTime = daySchedule.get(i);
            AvailableDateTime nextTime = null;
            if (i + 1 < daySchedule.size()) {
                nextTime = daySchedule.get(i + 1);
            }
            if (time.getStartTime().isAfter(prevTime.getEndTime())) {
                if (nextTime == null ||
                    time.getEndTime().isBefore(nextTime.getStartTime())
                ) {
                    return true;
                }
            }
        }
        // Exhausted the search
        return false;
    }
}
