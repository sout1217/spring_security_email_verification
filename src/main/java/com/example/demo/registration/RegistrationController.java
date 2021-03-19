package com.example.demo.registration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {

        String message = registrationService.register(request);

        return ResponseEntity.ok(message);
    }

    @GetMapping(path="confirm")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token) {

        String result = registrationService.confirmToken(token);

        return ResponseEntity.ok(result);
    }
}
