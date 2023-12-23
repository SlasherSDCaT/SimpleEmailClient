package ru.belosludtsev.kursach.emailclient.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.belosludtsev.kursach.emailclient.entities.EmailAccount;
import ru.belosludtsev.kursach.emailclient.entities.EmailAccountDetails;
import ru.belosludtsev.kursach.emailclient.entities.MessageIn;
import ru.belosludtsev.kursach.emailclient.entities.MessageOut;
import ru.belosludtsev.kursach.emailclient.errors.EmailReceiverIsNotFound;
import ru.belosludtsev.kursach.emailclient.repositories.EmailAccountRepositories;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailAccountServices implements UserDetailsService{
    private final EmailAccountRepositories emailAccountRepositories;
    public List<EmailAccount> findAll(){
        return emailAccountRepositories.findAll();
    }
    public EmailAccount findOne(int id){
        return emailAccountRepositories.findById(id).orElse(null);
    }
    public EmailAccount findByEmail(String email){
        Optional<EmailAccount> emailAccount = emailAccountRepositories.findByEmail(email);
        return emailAccount.orElse(null);
    }
    public List<MessageIn> findMessagesIn(int id, Boolean mark, Boolean sort){
        if(mark == null && (sort == null || !sort) ){
            return findMessageIn(id);
        }
        else if(mark != null && (sort == null || !sort) ){
            return findMessageIn(id, mark);
        }
        else if(mark == null && sort){
            return emailAccountRepositories.findById(id)
                    .map(emailAccount -> emailAccount.getMessagesIn()
                     .stream()
                     .sorted(Comparator.comparing(MessageIn::getEmailReceiver))
                     .collect(Collectors.toList()))
                    .orElse(null);
        }
        else{
            return emailAccountRepositories.findById(id)
                    .map(emailAccount -> emailAccount.getMessagesIn()
                    .stream()
                    .filter(messageIn -> messageIn.isMark() == mark)
                    .sorted(Comparator.comparing(MessageIn::getEmailReceiver))
                    .collect(Collectors.toList()))
                    .orElse(null);
        }

    }

    private List<MessageIn> findMessageIn(int id, Boolean mark) {
        return emailAccountRepositories.findById(id)
                .map(emailAccount -> emailAccount.getMessagesIn()
                .stream()
                .filter(messageIn -> messageIn.isMark() == mark)
                .sorted(Comparator.comparing(MessageIn::getTimeDeparture).reversed())
                .collect(Collectors.toList()))
                .orElse(null);
    }

    private List<MessageIn> findMessageIn(int id) {
        return emailAccountRepositories.findById(id)
                .map(emailAccount -> emailAccount.getMessagesIn()
                .stream()
                .sorted(Comparator.comparing(MessageIn::getTimeDeparture).reversed())
                .collect(Collectors.toList()))
                .orElse(null);
    }

    public List<MessageOut> findMessagesOut(int id, Boolean mark, Boolean sort){
        if(mark == null && (sort == null || !sort)){
            return findMessageOut(id);
        }
        else if(mark != null && (sort == null || !sort) ){
            return findMessageOut(id, mark);
        }
        else if(mark == null && sort){
            return emailAccountRepositories.findById(id)
                    .map(emailAccount -> emailAccount.getMessagesOut()
                    .stream()
                    .sorted(Comparator.comparing(MessageOut::getEmailSender))
                    .collect(Collectors.toList()))
                    .orElse(null);
        }
        else{
            return emailAccountRepositories.findById(id)
                    .map(emailAccount -> emailAccount.getMessagesOut()
                    .stream()
                    .filter(messageIn -> messageIn.isMark() == mark)
                    .sorted(Comparator.comparing(MessageOut::getEmailSender))
                    .collect(Collectors.toList()))
                    .orElse(null);
        }
    }

    private List<MessageOut> findMessageOut(int id, Boolean mark) {
        return emailAccountRepositories.findById(id)
                .map(emailAccount -> emailAccount.getMessagesOut()
                .stream()
                .filter(messageIn -> messageIn.isMark() == mark)
                .sorted(Comparator.comparing(MessageOut::getTimeDeparture).reversed())
                .collect(Collectors.toList()))
                .orElse(null);
    }

    private List<MessageOut> findMessageOut(int id) {
        return emailAccountRepositories.findById(id)
                .map(emailAccount -> emailAccount.getMessagesOut()
                .stream()
                .sorted(Comparator.comparing(MessageOut::getTimeDeparture).reversed())
                .collect(Collectors.toList()))
                .orElse(null);
    }
    public MessageIn findMessageIn(int id, int idMessage){
        Optional<EmailAccount> emailAccount = emailAccountRepositories.findById(id);
        return emailAccount.map(account -> account.getMessagesIn()
                .stream()
                .filter(messageIn -> messageIn.getId() == idMessage)
                .findFirst()
                .orElse(null)).orElse(null);
    }
    public MessageOut findMessageOut(int id, int idMessage){
        Optional<EmailAccount> emailAccount = emailAccountRepositories.findById(id);
        return emailAccount.map(account -> account.getMessagesOut()
                .stream()
                .filter(messageOut -> messageOut.getId() == idMessage)
                .findFirst()
                .orElse(null)).orElse(null);
    }

    public List<MessageIn> searchMessagesIn(int id, String text){
        return emailAccountRepositories.findById(id)
                .map(emailAccount -> emailAccount.getMessagesIn()
                .stream()
                .filter(messageIn -> messageIn.getContent().contains(text))
                .collect(Collectors.toList())).orElse(null);
    }
    public List<MessageOut> searchMessagesOut(int id, String text){
        return emailAccountRepositories.findById(id)
                .map(emailAccount -> emailAccount.getMessagesOut()
                        .stream()
                        .filter(messageIn -> messageIn.getContent().contains(text))
                        .collect(Collectors.toList())).orElse(null);
    }
    @Transactional
    public void markMessageIn(int id, int idMessage, boolean mark){
        findMessageIn(id, idMessage).setMark(mark);
    }
    @Transactional
    public void markMessageOut(int id, int idMessage, boolean mark){
        findMessageOut(id, idMessage).setMark(mark);
    }
    @Transactional
    public void save(EmailAccount emailAccount){
        emailAccountRepositories.save(emailAccount);
    }
    @Transactional
    public void update(int id, EmailAccount updateEmailAccount){
        updateEmailAccount.setId(id);
        emailAccountRepositories.save(updateEmailAccount);
    }
    @Transactional
    public void delete(int id){
        emailAccountRepositories.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<EmailAccount> emailAccount = emailAccountRepositories.findByEmail(username);
        if(emailAccount.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        return new EmailAccountDetails(emailAccount.get());
    }
}
