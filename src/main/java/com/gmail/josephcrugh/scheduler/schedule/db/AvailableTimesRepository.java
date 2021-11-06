package com.gmail.josephcrugh.scheduler.schedule.db;

import com.gmail.josephcrugh.scheduler.registration.db.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface AvailableTimesRepository
        extends JpaRepository<AvailableDateTime, Long> {

    // TODO: order
    @Query("SELECT s FROM AvailableDateTime s WHERE s.user=?1 AND s.day=?2")
    List<AvailableDateTime> findAllByUser(RegisteredUser user, DayOfWeek day);

}
