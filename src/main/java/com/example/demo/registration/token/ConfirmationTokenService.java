package com.example.demo.registration.token;

import com.example.demo.appuser.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public String saveConfirmationToken(ConfirmationToken token) {
        ConfirmationToken savedToken = confirmationTokenRepository.save(token);
        return savedToken.getToken();
    }

    public Optional<ConfirmationToken> findByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @Transactional
    public void updateConfirmationToken(ConfirmationToken token) {

        token.setConfirmedAt(LocalDateTime.now());

        token.getAppUser().setEnabled(true);
    }

    public Optional<ConfirmationToken> findByUser(AppUser appUser) {
        return confirmationTokenRepository.findByAppUser(appUser);
    }
}
