package com.gmail.josephcrugh.scheduler.schedule.db;

import com.gmail.josephcrugh.scheduler.registration.db.RegisteredUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;


@Entity
@Table(name = "available_times")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class AvailableDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_userid")
    private RegisteredUser user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;
    @NotNull
    @DateTimeFormat(pattern = "HH:MM")
    private LocalTime startTime;
    @NotNull
    @DateTimeFormat(pattern = "HH:MM")
    private LocalTime endTime;
}
