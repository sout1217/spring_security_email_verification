package com.example.demo.registration;

import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserRole;
import com.example.demo.appuser.AppUserService;
import com.example.demo.email.EmailSender;
import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    @Transactional
    public String register(RegistrationRequest request) {

        // 이메일 유효성 검사 체크
        boolean isValidEmail = emailValidator.test(request.getEmail());

        // 유효성 검사 틀린 경우 error
        if (!isValidEmail) throw new IllegalStateException("email not valid");

        String token = appUserService.signUpUser(new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                AppUserRole.USER
        ));

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;

        emailSender.send(request.getEmail(), buildEmail(request.getFirstName(), link));

//        return token;
        return "Sending tokens by email";
    }

    private String buildEmail(String firstName, String link) {
        return String.format("%s 님 해당 링크를 클릭하여 인증을 해주세요 <a>%s</a>", firstName, link);
    }

    @Transactional
    public String confirmToken(String token) {

        // 해당하는 토큰을 가져옵니다
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token).orElseThrow(() -> new IllegalStateException("token not found"));

        // 이미 확인 된 이메일
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        // 만료날짜 가져오기
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        // 만료 된 날짜가 현재 날짜보다 이전 인 경우
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        // 이메일 인증 확인 저장
        confirmationTokenService.updateConfirmationToken(confirmationToken);

        return "Certification verification";
    }
}
