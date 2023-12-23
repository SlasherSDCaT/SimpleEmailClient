package ru.belosludtsev.kursach.emailclient.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.belosludtsev.kursach.emailclient.entities.EmailAccount;
import ru.belosludtsev.kursach.emailclient.entities.MessageIn;
import ru.belosludtsev.kursach.emailclient.entities.MessageOut;
import ru.belosludtsev.kursach.emailclient.entities.TypeMessage;
import ru.belosludtsev.kursach.emailclient.errors.EmailReceiverIsNotFound;
import ru.belosludtsev.kursach.emailclient.repositories.MessageOutRepositories;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageOutServices {
    private final MessageOutRepositories messageOutRepositories;
    private final EmailAccountServices emailAccountServices;

    public List<MessageOut> findAll(){
        return messageOutRepositories.findAll();
    }

    public MessageOut findOne(int id){
        return messageOutRepositories.findById(id).orElse(null);
    }

    @Transactional
    public void save(int id, MessageIn messageIn) throws EmailReceiverIsNotFound {
        var messageOut = MessageOut.builder()
                        .type(TypeMessage.OUT_BOX).content(messageIn.getContent())
                        .emailReceiver(messageIn.getEmailReceiver())
                        .emailSender(messageIn.getSender().getEmail())
                        .timeDeparture(LocalDateTime.now())
                        .build();
        EmailAccount receiver = emailAccountServices.findByEmail(messageIn.getEmailReceiver());
        if(receiver == null){
            throw new EmailReceiverIsNotFound("Email получателя не существует");
        }
        messageOut.setReceiver(receiver);
        receiver.addMessageOut(messageOut);
        messageOutRepositories.save(messageOut);
    }

    @Transactional
    public void update(int id, MessageOut updateMessage){
        updateMessage.setId(id);
        messageOutRepositories.save(updateMessage);
    }
    @Transactional
    public void delete(int id){
        messageOutRepositories.deleteById(id);
    }
}
