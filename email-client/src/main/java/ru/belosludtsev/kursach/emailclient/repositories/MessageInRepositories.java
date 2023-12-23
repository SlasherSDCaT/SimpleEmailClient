package ru.belosludtsev.kursach.emailclient.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.belosludtsev.kursach.emailclient.entities.MessageIn;

@Repository
public interface MessageInRepositories extends JpaRepository<MessageIn, Integer> {
}
