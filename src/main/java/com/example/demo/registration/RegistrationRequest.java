package com.example.demo.registration;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class RegistrationRequest {

    String firstName;
    String lastName;
    String email;
    String password;

}
