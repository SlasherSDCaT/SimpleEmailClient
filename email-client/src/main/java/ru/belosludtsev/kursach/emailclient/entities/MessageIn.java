package ru.belosludtsev.kursach.emailclient.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="MessageIn")
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class MessageIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private TypeMessage type;
    private String content;
    private String emailReceiver;
    private LocalDateTime timeDeparture;
    private boolean mark;
    @ManyToOne
    @JoinColumn(name="id_emailAccount", referencedColumnName = "id")
    @JsonBackReference
    private EmailAccount sender;
}