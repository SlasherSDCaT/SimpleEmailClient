package ru.belosludtsev.kursach.emailclient.dao.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SignUpRequest {
    private String name;
    private String surname;
    private String email;
    private String password;
}
