package ru.belosludtsev.kursach.emailclient.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.belosludtsev.kursach.emailclient.entities.EmailAccount;
import ru.belosludtsev.kursach.emailclient.entities.EmailAccountDetails;

@Service
public class AuthenticatedEmailAccountService {
    public EmailAccount getAuthenticatedEmailAccount(){
        EmailAccountDetails emailAccountDetails = (EmailAccountDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return emailAccountDetails.getEmailAccount();
    }
}
