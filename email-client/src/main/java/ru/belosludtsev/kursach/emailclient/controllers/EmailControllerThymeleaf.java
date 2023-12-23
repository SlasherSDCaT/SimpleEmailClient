package ru.belosludtsev.kursach.emailclient.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.belosludtsev.kursach.emailclient.entities.EmailAccount;
import ru.belosludtsev.kursach.emailclient.services.AuthenticatedEmailAccountService;
import ru.belosludtsev.kursach.emailclient.services.EmailAccountServices;
import ru.belosludtsev.kursach.emailclient.services.MessageInServices;
import ru.belosludtsev.kursach.emailclient.services.MessageOutServices;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/emailTh")
@RequiredArgsConstructor
public class EmailControllerThymeleaf {
    private final EmailAccountServices emailAccountServices;
    private final MessageInServices messageInServices;
    private final MessageOutServices messageOutServices;
    private List<EmailAccount> emailAccountsAuto;
    private final AuthenticatedEmailAccountService authenticatedEmailAccountService;
    @GetMapping("")
    public String showMainPage(){
        return "mail/show";
    }
    @GetMapping("/person")
    public String showMainPage(Model model, HttpServletRequest request){
        if(emailAccountsAuto == null){
            emailAccountsAuto = new ArrayList<>();
        }
//        EmailAccount emailAccount = authenticatedEmailAccountService.getAuthenticatedEmailAccount();
//        System.out.println(emailAccount.getEmail());
//        emailAccountsAuto.add(emailAccount);
//        model.addAttribute("emailAccounts", emailAccountsAuto);
        System.out.println(request.getHeader("Authorization"));

        return "/mail/person";
    }
    @GetMapping("/login")
    public String showLogin(){
        return "mail/login";
    }
}
