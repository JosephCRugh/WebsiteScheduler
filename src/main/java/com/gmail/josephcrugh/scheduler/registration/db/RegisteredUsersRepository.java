package com.gmail.josephcrugh.scheduler.registration.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisteredUsersRepository
        extends JpaRepository<RegisteredUser, Long> {

    Optional<RegisteredUser> findByEmail(String email);

}
