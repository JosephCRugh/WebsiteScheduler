package com.gmail.josephcrugh.scheduler.registration;

import com.gmail.josephcrugh.scheduler.registration.UserByEmailNotFoundException;
import com.gmail.josephcrugh.scheduler.registration.db.RegisteredUser;
import com.gmail.josephcrugh.scheduler.registration.db.RegisteredUsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Interfaces between the database repository for registered
 * users and the data object RegisteredUser
 */
@Service
@Transactional
@AllArgsConstructor
public class RegisteredUsersService implements UserDetailsService {

    private final RegisteredUsersRepository registeredUsersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return registeredUsersRepository.findByEmail(email)
                // Throw an exception if the user could not be found given the email
                .orElseThrow(() -> new UserByEmailNotFoundException(email));
    }

    public boolean userExistByEmail(String email) {
        return findUserByEmail(email).isPresent();
    }

    public Optional<RegisteredUser> findUserByEmail(String email) {
        return registeredUsersRepository.findByEmail(email);
    }

    public void storeNewUser(RegisteredUser user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        registeredUsersRepository.save(user);
    }
}
