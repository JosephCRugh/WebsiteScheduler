package com.gmail.josephcrugh.scheduler.registration.db;

import com.gmail.josephcrugh.scheduler.registration.UserByEmailNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Interfaces between the database repository for registered
 * users and the data object RegisteredUser
 */
@Service
@AllArgsConstructor
public class RegisteredUsersService implements UserDetailsService {

    private final RegisteredUsersRepository registeredUsersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return registeredUsersRepository.findByEmail(email)
                // Create a user from the database object
                        .map(registeredUser ->
                                User.builder()
                                        .username(registeredUser.getEmail())
                                        .password(registeredUser.getPassword())
                                        .roles("user")
                                        .build())
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
