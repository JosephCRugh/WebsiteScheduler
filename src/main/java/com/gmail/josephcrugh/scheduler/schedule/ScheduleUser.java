package com.gmail.josephcrugh.scheduler.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ScheduleUser {
    private String email;
    private String firstName;
    private String lastName;
}
