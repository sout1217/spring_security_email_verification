package com.example.demo.appuser;

import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                    new UsernameNotFoundException(
                            String.format(USER_NOT_FOUND, email)));
    }

    public String signUpUser(AppUser appUser) {

        // 이메일이 이미 있는 지 확인
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();

        // 이미 받은 이메일
        if (userExists) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email
            // 이메일은 존재하지만 confirmAt 이 null 인 경우는 메일만 보낸다
//            ConfirmationToken confirmationToken = confirmationTokenService.findByUser(appUser).orElseThrow(() -> new IllegalStateException("not existed token"));

            // 확인 된 경우
//            if (confirmationToken.isConfirm()) {
                throw new IllegalStateException("email already taken");
//            }

        } else {

            String encodePassword = passwordEncoder.encode(appUser.getPassword());

            appUser.setPassword(encodePassword);

            appUserRepository.save(appUser);
        }

        // TODO: Send confirmation token

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        // TODO: Send Email
        return confirmationTokenService.saveConfirmationToken(confirmationToken);
    }
}
