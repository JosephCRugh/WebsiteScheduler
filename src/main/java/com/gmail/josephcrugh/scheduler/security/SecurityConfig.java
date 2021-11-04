package com.gmail.josephcrugh.scheduler.security;

import com.gmail.josephcrugh.scheduler.registration.db.RegisteredUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final RegisteredUsersService registeredUsersService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // Although it is useful for preventing cross site request forgery it requires
                // cookies so that the server may send tokens
                .csrf().disable()
                // Path authorization
                .authorizeRequests()
                    .antMatchers("/register").permitAll()
                    .antMatchers("/*").hasRole("user")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                    .successHandler(new SuccessLoginHandler())
                    .loginPage("/login")
                    .permitAll()
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(registeredUsersService);
        return provider;
    }

}
