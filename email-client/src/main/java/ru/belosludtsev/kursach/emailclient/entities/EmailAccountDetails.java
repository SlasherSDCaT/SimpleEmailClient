package ru.belosludtsev.kursach.emailclient.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.belosludtsev.kursach.emailclient.entities.EmailAccount;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Data
public class EmailAccountDetails implements UserDetails {
    private final EmailAccount emailAccount;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(emailAccount.getRole().name()));
    }

    @Override
    public String getPassword() {
        return emailAccount.getPassword();
    }

    @Override
    public String getUsername() {
        return emailAccount.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
