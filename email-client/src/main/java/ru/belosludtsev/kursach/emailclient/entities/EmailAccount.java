package ru.belosludtsev.kursach.emailclient.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="EmailAccount")
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class EmailAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "sender")
    @JsonManagedReference
    private List<MessageIn> messagesIn;
    @OneToMany(mappedBy = "receiver")
    @JsonManagedReference
    private List<MessageOut> messagesOut;

    public void addMessageIn(MessageIn message){
        if(this.messagesIn == null){
            messagesIn = new ArrayList<>();
        }
        messagesIn.add(message);
    }
    public void addMessageOut(MessageOut message){
        if(this.messagesOut == null){
            messagesOut = new ArrayList<>();
        }
        messagesOut.add(message);
    }
}
