package ru.belosludtsev.kursach.emailclient.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.belosludtsev.kursach.emailclient.entities.EmailAccount;
import ru.belosludtsev.kursach.emailclient.entities.MessageIn;
import ru.belosludtsev.kursach.emailclient.entities.TypeMessage;
import ru.belosludtsev.kursach.emailclient.errors.EmailReceiverIsNotFound;
import ru.belosludtsev.kursach.emailclient.repositories.MessageInRepositories;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageInServices {
    private final MessageInRepositories messageInRepositories;
    private final EmailAccountServices emailAccountServices;
    public List<MessageIn> findAll(){
        return messageInRepositories.findAll();
    }
    public MessageIn findOne(int id){
        return messageInRepositories.findById(id).orElse(null);
    }
    @Transactional
    public void save(int id, MessageIn message) throws EmailReceiverIsNotFound {
        if(!isEmailReceiverExists(message.getEmailReceiver())){
           throw  new EmailReceiverIsNotFound("Email получателя не существует");
        }
        saveIn(id,message);
        messageInRepositories.save(message);
    }

    private boolean isEmailReceiverExists(String emailReceiver) {
        EmailAccount emailAccount = emailAccountServices.findByEmail(emailReceiver);
        if (emailAccount != null) return true;
        else return false;
    }

    private void saveIn(int id, MessageIn message) {
        EmailAccount emailAccount = emailAccountServices.findOne(id);
        message.setType(TypeMessage.IN_BOX);
        message.setTimeDeparture(LocalDateTime.now());
        message.setSender(emailAccount);
        emailAccount.addMessageIn(message);
    }

    @Transactional
    public void update(int id, MessageIn updateMessage){
        updateMessage.setId(id);
        messageInRepositories.save(updateMessage);
    }
    @Transactional
    public void delete(int id){
        messageInRepositories.deleteById(id);
    }
}
