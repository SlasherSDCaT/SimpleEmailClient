package ru.belosludtsev.kursach.emailclient.dao.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class JwtAuthenticationResponse {
    private String token;
}
