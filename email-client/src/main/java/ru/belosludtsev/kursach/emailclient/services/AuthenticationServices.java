package ru.belosludtsev.kursach.emailclient.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.belosludtsev.kursach.emailclient.dao.request.SignInRequest;
import ru.belosludtsev.kursach.emailclient.dao.request.SignUpRequest;
import ru.belosludtsev.kursach.emailclient.dao.response.JwtAuthenticationResponse;
import ru.belosludtsev.kursach.emailclient.entities.EmailAccount;
import ru.belosludtsev.kursach.emailclient.entities.EmailAccountDetails;
import ru.belosludtsev.kursach.emailclient.entities.Role;
import ru.belosludtsev.kursach.emailclient.repositories.EmailAccountRepositories;

@Service
@RequiredArgsConstructor
public class AuthenticationServices {
    private final EmailAccountRepositories emailAccountRepositories;
    private final PasswordEncoder passwordEncoder;
    private final JwtServices jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var emailAccount = EmailAccount
                .builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();
        emailAccountRepositories.save(emailAccount);
        var jwt = jwtService.generateToken(new EmailAccountDetails(emailAccount));
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
    public JwtAuthenticationResponse signIn(SignInRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var emailAccount = emailAccountRepositories.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(new EmailAccountDetails(emailAccount));
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}
