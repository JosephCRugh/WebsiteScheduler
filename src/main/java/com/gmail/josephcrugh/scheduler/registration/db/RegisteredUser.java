package com.gmail.josephcrugh.scheduler.registration.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "registered_users")
@NoArgsConstructor
@Getter
@Setter
public class RegisteredUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max = 30)
    private String firstName;
    @NotEmpty
    @Size(max = 30)
    private String lastName;
    @Email
    private String email;
    @NotEmpty
    @Size(max = 300)
    private String password;

    public RegisteredUser(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
