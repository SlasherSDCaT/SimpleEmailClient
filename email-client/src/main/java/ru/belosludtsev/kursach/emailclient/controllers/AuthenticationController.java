package ru.belosludtsev.kursach.emailclient.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.belosludtsev.kursach.emailclient.dao.request.SignInRequest;
import ru.belosludtsev.kursach.emailclient.dao.request.SignUpRequest;
import ru.belosludtsev.kursach.emailclient.dao.response.JwtAuthenticationResponse;
import ru.belosludtsev.kursach.emailclient.services.AuthenticationServices;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationServices authenticationService;
    @PostMapping("/signUp")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signUp(request));
    }
    @PostMapping("/signIn")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }
}
