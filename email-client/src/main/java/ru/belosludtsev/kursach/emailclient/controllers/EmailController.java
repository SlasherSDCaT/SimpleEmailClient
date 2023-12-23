package ru.belosludtsev.kursach.emailclient.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.belosludtsev.kursach.emailclient.entities.EmailAccount;
import ru.belosludtsev.kursach.emailclient.entities.EmailAccountDetails;
import ru.belosludtsev.kursach.emailclient.entities.MessageIn;
import ru.belosludtsev.kursach.emailclient.entities.MessageOut;
import ru.belosludtsev.kursach.emailclient.errors.EmailReceiverIsNotFound;
import ru.belosludtsev.kursach.emailclient.services.AuthenticatedEmailAccountService;
import ru.belosludtsev.kursach.emailclient.services.EmailAccountServices;
import ru.belosludtsev.kursach.emailclient.services.MessageInServices;
import ru.belosludtsev.kursach.emailclient.services.MessageOutServices;

import java.util.List;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailAccountServices emailAccountServices;
    private final MessageInServices messageInServices;
    private final MessageOutServices messageOutServices;
    private final AuthenticatedEmailAccountService authenticatedEmailAccountService;

    @GetMapping("/all")
    public List<EmailAccount> index(Model model){
        return emailAccountServices.findAll();
    }

    @GetMapping("")
    public EmailAccount show(Model model){
        EmailAccount emailAccount = authenticatedEmailAccountService.getAuthenticatedEmailAccount();
        return emailAccountServices.findOne(emailAccount.getId());
    }
    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(@RequestBody MessageIn messageIn) throws EmailReceiverIsNotFound {
        try{
            EmailAccount emailAccount = authenticatedEmailAccountService.getAuthenticatedEmailAccount();
            messageInServices.save(emailAccount.getId(), messageIn);
            messageOutServices.save(emailAccount.getId(), messageIn);
            return ResponseEntity.ok("Message sent successfully");

        } catch (EmailReceiverIsNotFound e){
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/getMessagesIn")
    public List<MessageIn> getMessagesIn(@RequestParam(value="mark", required = false) Boolean mark,
                                         @RequestParam(value="sortedByName", required = false) Boolean sort,
                                         @RequestParam(value="text", required = false) String text){
        EmailAccount emailAccount = authenticatedEmailAccountService.getAuthenticatedEmailAccount();
        if (text == null) {
            return emailAccountServices.findMessagesIn(emailAccount.getId(), mark, sort);
        } else {
            return emailAccountServices.searchMessagesIn(emailAccount.getId(), text);
        }
    }
    @GetMapping("/getMessagesOut")
    public List<MessageOut> getMessagesOut(@RequestParam(value="mark", required = false) Boolean mark,
                                           @RequestParam(value="sortedByName", required = false) Boolean sort,
                                           @RequestParam(value="text", required = false) String text){
        EmailAccount emailAccount = authenticatedEmailAccountService.getAuthenticatedEmailAccount();
        if (text == null) {
            return emailAccountServices.findMessagesOut(emailAccount.getId(), mark, sort);
        } else {
            return emailAccountServices.searchMessagesOut(emailAccount.getId(), text);
        }
    }

    @GetMapping("/getMessagesIn/{idMessage}")
    public MessageIn getMessageIn(@PathVariable("idMessage") int idMessage){
        EmailAccount emailAccount = authenticatedEmailAccountService.getAuthenticatedEmailAccount();
        return  emailAccountServices.findMessageIn(emailAccount.getId(), idMessage);
    }
    @GetMapping("/getMessagesOut/{idMessage}")
    public MessageOut getMessageOut(@PathVariable("idMessage") int idMessage){
        EmailAccount emailAccount = authenticatedEmailAccountService.getAuthenticatedEmailAccount();
        return  emailAccountServices.findMessageOut(emailAccount.getId(), idMessage);
    }
    @PostMapping("/getMessagesIn/{idMessage}")
    public void markMessageIn(@PathVariable("idMessage") int idMessage,
                            @RequestParam(value="mark", required = false) boolean mark){
        EmailAccount emailAccount = authenticatedEmailAccountService.getAuthenticatedEmailAccount();
        emailAccountServices.markMessageIn(emailAccount.getId(), idMessage, mark);
    }
    @PostMapping("/getMessagesOut/{idMessage}")
    public void markMessageOut(@PathVariable("idMessage") int idMessage,
                            @RequestParam(value="mark", required = false) boolean mark){
        EmailAccount emailAccount = authenticatedEmailAccountService.getAuthenticatedEmailAccount();
        emailAccountServices.markMessageOut(emailAccount.getId(), idMessage, mark);

    }

}
